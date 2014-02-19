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
package org.openmrs.module.drughistory.api.impl;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.openmrs.module.drughistory.api.DrugEventService;
import org.openmrs.module.drughistory.api.db.DrugEventDAO;
import org.openmrs.module.drughistory.api.db.DrugEventTriggerDAO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link DrugEventService}.
 */
public class DrugEventServiceImpl extends BaseOpenmrsService implements DrugEventService {

    protected final Log log = LogFactory.getLog(this.getClass());

    private DrugEventDAO dao;
    private DrugEventTriggerDAO triggerDAO;

    /**
     * @param dao the dao to set
     */
    public void setDao(DrugEventDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public DrugEventDAO getDao() {
        return dao;
    }

    public DrugEventTriggerDAO getTriggerDAO() {
        return triggerDAO;
    }

    public void setTriggerDAO(DrugEventTriggerDAO triggerDAO) {
        this.triggerDAO = triggerDAO;
    }

    @Override
    public void generateAllDrugEvents(final Date sinceWhen) {
        generateAllDrugEvents(null,sinceWhen);
    }

    @Override
    public void generateAllDrugEvents(Person person, Date sinceWhen) {
        if (sinceWhen != null) {
            if (sinceWhen.compareTo(new Date()) > 0) {
                throw new IllegalArgumentException("Date: " + sinceWhen + " should be greater than the date of today");
            } else {

            }
        } else {
            //All drug events.
        }
    }


    @Override
    public void generateDrugEventsFromTrigger(final DrugEventTrigger trigger, final Date sinceWhen) {
        generateDrugEventsFromTrigger(null,trigger,sinceWhen);
    }

    @Override
    public void generateDrugEventsFromTrigger(final Person person, final DrugEventTrigger trigger, final Date sinceWhen) {
        if(trigger == null) {
            throw  new IllegalArgumentException("trigger cannot be null");
        }
        if(trigger.getEventType() == null) {
            throw new IllegalArgumentException("Trigger's eventType can not be null");
        }
        if (sinceWhen != null && sinceWhen.compareTo(new Date()) > 0) {
            throw new IllegalArgumentException("Date: " + sinceWhen + " should be earlier than or equal to today");
        }
        dao.generateDrugEventsFromTrigger(person,trigger,sinceWhen);
    }


    @Override
    public List<DrugEventTrigger> getAllDrugEventTriggers(Boolean includeRetired) {
        return triggerDAO.getAllDrugEventTriggers(includeRetired);
    }

    @Override
    public List<DrugEvent> getAllDrugEvents(Date sinceWhen){
        if (sinceWhen != null && sinceWhen.compareTo(new Date()) > 0) {
            throw new IllegalArgumentException("Date: " + sinceWhen + " should be earlier than or equal to today");
        }
        return dao.getAllDrugEvents(sinceWhen);
    }

	@Override
	public void purgeAllDrugEvents() {
		dao.purgeAllDrugEvents();
	}
}