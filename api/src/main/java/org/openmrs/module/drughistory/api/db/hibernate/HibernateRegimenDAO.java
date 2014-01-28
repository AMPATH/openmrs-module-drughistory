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

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.drughistory.Regimen;
import org.openmrs.module.drughistory.api.db.RegimenDAO;

import java.util.List;

public class HibernateRegimenDAO implements RegimenDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Regimen> getAllRegimens(Boolean includeRetired) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Regimen.class);

		if (includeRetired == null || !includeRetired) {
			c.add(Restrictions.eq("retired", false));
		}

		return (List<Regimen>) c.list();
	}

	@Override
	public Regimen getRegimen(Integer artRegimenId) {
		return (Regimen) sessionFactory.getCurrentSession()
				.get(Regimen.class, artRegimenId);
	}

	@Override
	public Regimen saveRegimen(Regimen regimen) {
		sessionFactory.getCurrentSession().saveOrUpdate(regimen);
		return regimen;
	}

	@Override
	public void purgeRegimen(Regimen regimen) {
		sessionFactory.getCurrentSession().delete(regimen);
	}
}
