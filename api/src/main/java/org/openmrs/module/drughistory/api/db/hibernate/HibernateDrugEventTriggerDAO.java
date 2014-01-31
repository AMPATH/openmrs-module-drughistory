package org.openmrs.module.drughistory.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.openmrs.module.drughistory.api.db.DrugEventTriggerDAO;

import java.util.List;

/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

public class HibernateDrugEventTriggerDAO implements DrugEventTriggerDAO {
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
    public void saveDrugEventTrigger(DrugEventTrigger drugEventTrigger) {
        if(drugEventTrigger!=null) {
            getSessionFactory().getCurrentSession().saveOrUpdate(drugEventTrigger);
        }
    }

    @Override
    public DrugEventTrigger getDrugEventTrigger(Integer drugEventTriggerId) {
        return (DrugEventTrigger)getSessionFactory().getCurrentSession().get(DrugEventTrigger.class,drugEventTriggerId);
    }

    @Override
    public List<DrugEventTrigger> getAllDrugEventTriggers(boolean includeRetired) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(DrugEventTrigger.class);

        if(!includeRetired) {
           criteria.add(Restrictions.eq("retired",false));
        }
        return criteria.list();
    }

    @Override
    public List<DrugEventTrigger> getDrugEventTriggers(Concept question,boolean includeRetired) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(DrugEventTrigger.class);

        if(!includeRetired) {
            criteria.add(Restrictions.eq("retired",false));
        }

        if(question!=null) {
            criteria.add(Restrictions.eq("question",question.getConceptId()));
        }

        return criteria.list();
    }

    @Override
    public void purgeDrugEventTrigger(DrugEventTrigger drugEventTrigger) {
        if(drugEventTrigger != null) {
            getSessionFactory().getCurrentSession().delete(drugEventTrigger);
        }
    }
}
