/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.drughistory.api.db.hibernate;

import liquibase.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Cohort;
import org.openmrs.Encounter;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.openmrs.module.drughistory.api.db.DrugEventDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * It is a default implementation of  {@link DrugEventDAO}.
 */
public class HibernateDrugEventDAO implements DrugEventDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	private SessionFactory sessionFactory;

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public void saveDrugEvent(DrugEvent drugEvent) throws DAOException {
		if (drugEvent != null) {
			getSessionFactory().getCurrentSession().saveOrUpdate(drugEvent);
		}
	}

	@Override
	public void saveDrugEvents(List<DrugEvent> drugEvents, int batchSize) throws DAOException {
		//TODO To be implemented with Hibernate batch processing.
		for (DrugEvent de : drugEvents) {
			saveDrugEvent(de);
		}
	}

	@Override
	public List<DrugEvent> getDrugEventsForPatient(Person person) throws DAOException {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(DrugEvent.class);
		criteria.add(Restrictions.eq("person", person));
		return criteria.list();
	}

	@Override
	public List<DrugEvent> getDrugEventsForPatient(Person person, Date sinceWhen) throws DAOException {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(DrugEvent.class);
		criteria.add(Restrictions.eq("person", person)).add(Restrictions.ge("dateOccurred", sinceWhen));
		return criteria.list();
	}

	@Override
	public List<DrugEvent> getEncounterDrugEvents(Encounter encounter) throws DAOException {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(DrugEvent.class);
		criteria.add(Restrictions.eq("encounter", encounter));
		return criteria.list();
	}

	@Override
	public void purgeDrugEvent(DrugEvent drugEvent) throws DAOException {
		if (drugEvent != null) {
			getSessionFactory().getCurrentSession().delete(drugEvent);
		}
	}

	@Override
	public int purgeAllDrugEvents() throws DAOException {
		String hql = "DELETE FROM DrugEvent";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		return query.executeUpdate();
	}

	@Override
	public int purgeDrugEventsForPatient(Person person) throws DAOException {
		String hql = "DELETE FROM DrugEvent WHERE personId = :personId";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		query.setInteger("personId", person.getPersonId());
		return query.executeUpdate();
	}

	@Override
	public void generateDrugEventsFromTrigger(Person person, DrugEventTrigger trigger, Date sinceWhen) {
		Query query;

		if (trigger.getCustomQuery() != null) {

			//The query generates and inserts the drug events.
			query = getSessionFactory().getCurrentSession().createSQLQuery(trigger.getCustomQuery());
			query.executeUpdate();

			return;
		}

		String sql = "SELECT person_id, encounter_id, obs_datetime" +
				" FROM obs o" +
				" where o.concept_id in (:conceptList)";

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("conceptList", trigger.getQuestions());

		if (!trigger.getAnswers().isEmpty()) {
			sql += " and o.value_coded in (:answerList)";
			m.put("answerList", trigger.getAnswers());
		}

		if (person != null) {
			sql += " and o.person_id = :personId";
			m.put("personId", person);
		}

		if (sinceWhen != null) {
			sql += " and o.obs_datetime >= :sinceWhen";
			m.put("sinceWhen", sinceWhen);
		}

		query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		mapIntoQuery(query, m);

		List<Object[]> obsList = query.list();

		//Create drug event for each obs returned and save them.

		String insertPrefix = "INSERT INTO drughistory_drugevent" +
				" (person_id, encounter_id, concept_id, concept_reason_id, date_occurred, drug_event_type, uuid)" +
				" VALUES ";
		List<String> inserts = new ArrayList<String>();

		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (Object[] ob : obsList) {

			Integer personId = (Integer) ob[0];
			Integer encounterId = (Integer) ob[1];
			Date dateOccurred = (Date) ob[2];

			List<String> parts = new ArrayList<String>();
			parts.add(personId == null ? "NULL" : personId.toString());
			parts.add(encounterId == null ? "NULL" : encounterId.toString());
			parts.add(trigger.getEventConcept() == null ? "NULL" : trigger.getEventConcept().getId().toString());
			parts.add(trigger.getEventReason() == null ? "NULL" : trigger.getEventReason().getId().toString());
			parts.add(dateOccurred == null ? "NULL" : "'" + sdf.format(dateOccurred) + "'");
			parts.add("'" + (trigger.getEventType() == null ? "NULL" : trigger.getEventType().getValue()) + "'");
			parts.add("'" + UUID.randomUUID().toString() + "'");

			inserts.add("(" + StringUtils.join(parts, ", ") + ")");

			if (i++ > 1000) {
				String insertStatement = insertPrefix + StringUtils.join(inserts, ",");
				sessionFactory.getCurrentSession().createSQLQuery(insertStatement).executeUpdate();
				i = 0;
				inserts.clear();
			}
		}

		if (!inserts.isEmpty()) {
			String insertStatement = insertPrefix + StringUtils.join(inserts, ",");
			sessionFactory.getCurrentSession().createSQLQuery(insertStatement).executeUpdate();
		}
	}

	private void mapIntoQuery(Query q, Map<String, Object> parameterValues) {
		for (Map.Entry<String, Object> e : parameterValues.entrySet()) {
			if (e.getValue() instanceof Collection) {
				q.setParameterList(e.getKey(), (Collection) e.getValue());
			} else if (e.getValue() instanceof Object[]) {
				q.setParameterList(e.getKey(), (Object[]) e.getValue());
			} else if (e.getValue() instanceof Cohort) {
				q.setParameterList(e.getKey(), ((Cohort) e.getValue()).getMemberIds());
			} else {
				q.setParameter(e.getKey(), e.getValue());
			}
		}
	}

	@Override
	public List<DrugEvent> getDrugEvents(Properties params) {

		if (params == null) {
			params = new Properties();
		}

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(DrugEvent.class);

		if (params.containsKey("person")) {
			criteria.add(Restrictions.eq("person", params.get("person")));
		}

		if (params.containsKey("since")) {
			criteria.add(Restrictions.ge("dateOccurred", params.get("since")));
		}

		criteria.addOrder(Order.asc("dateOccurred"));

		return criteria.list();
	}
}
