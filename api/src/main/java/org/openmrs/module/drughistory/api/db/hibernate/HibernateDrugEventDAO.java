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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.openmrs.module.drughistory.api.db.DrugEventDAO;

import java.util.Date;
import java.util.List;
import java.util.Properties;

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
        String hql = "DELETE FROM DrugEvent WHERE person_id = :personId";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setInteger("personId", person.getPersonId());
        return query.executeUpdate();
    }

	@Override
	public void generateDrugEventsFromTrigger(Person person, DrugEventTrigger trigger, Date sinceWhen) {
		if (trigger.getCustomQuery() != null) {
			Query query = getSessionFactory().getCurrentSession().createQuery(trigger.getCustomQuery());
			//The query generates and inserts the drug events.
			query.executeUpdate();
		} else {
			Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Obs.class);
			criteria.add(Restrictions.in("concept", trigger.getQuestions()));
			if (!trigger.getAnswers().isEmpty()) {
				criteria.add(Restrictions.eq("valueCoded", trigger.getAnswers()));
			}
            if (person != null) {
                criteria.add(Restrictions.eq("person", person));
            }
			if (sinceWhen != null) {
				criteria.add(Restrictions.ge("obsDatetime", sinceWhen));
			}

			List<Obs> obsList = criteria.list();
			//Create drug event for each obs returned and save them.
			for (Obs obs : obsList) {
				DrugEvent event = new DrugEvent(obs.getPerson(), obs.getConcept(), obs.getObsDatetime(), trigger.getEventType());
				event.setEncounter(obs.getEncounter());
				saveDrugEvent(event);
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
