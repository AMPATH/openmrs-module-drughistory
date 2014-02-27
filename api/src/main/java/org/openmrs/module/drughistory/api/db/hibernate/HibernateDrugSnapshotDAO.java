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
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.module.drughistory.DrugSnapshot;
import org.openmrs.module.drughistory.api.db.DrugSnapshotDAO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class HibernateDrugSnapshotDAO implements DrugSnapshotDAO {

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
	public List<DrugSnapshot> getDrugSnapshots(Properties params) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(DrugSnapshot.class);

		if (params != null) {
			if (params.containsKey("cohort") && params.get("cohort") instanceof Cohort) {
				// only retrieve snapshots for these person Ids
				Cohort cohort = (Cohort) params.get("cohort");
				criteria.add(Restrictions.in("person.personId", cohort.getMemberIds()));
			}
			if (params.containsKey("drugs") && params.get("drugs") instanceof Set) {
				// only get back snapshots related to these drugs
				Set<Integer> drugIds = new HashSet<Integer>();
				for (Concept c : (Set<Concept>) params.get("drugs")) {
					drugIds.add(c.getConceptId());
				}
				criteria.createAlias("concepts", "ack");
				criteria.add(Restrictions.in("ack.conceptId", drugIds));
			}
		}

		return criteria.list();
	}

	@Override
	public void saveSnapshots(List<DrugSnapshot> snapshots) {
		// TODO do this better
		for (DrugSnapshot snapshot : snapshots) {
			saveSnapshot(snapshot);
		}
	}

	private void saveSnapshot(DrugSnapshot snapshot) {
		getSessionFactory().getCurrentSession().saveOrUpdate(snapshot);
	}
}
