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
import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.Person;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.api.db.DrugEventDAO;

import java.util.Date;
import java.util.List;

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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveDrugEvents(List<DrugEvent> drugEvents) throws DAOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DrugEvent> getDrugEventsForPatient(Person person) throws DAOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DrugEvent> getDrugEventsForPatient(Person person, Date sinceWhen) throws DAOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DrugEvent> getEncounterDrugEvents(Encounter encounter) throws DAOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void purgeDrugEvent(DrugEvent drugEvent) throws DAOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void purgeAllDrugEvents() throws DAOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void purgeDrugEventsForPatient(Person person) throws DAOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}