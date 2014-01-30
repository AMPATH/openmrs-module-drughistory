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
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.openmrs.module.drughistory.api.DrugEventService;
import org.openmrs.module.drughistory.api.db.DrugEventDAO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link DrugEventService}.
 */
public class DrugEventServiceImpl extends BaseOpenmrsService implements DrugEventService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private DrugEventDAO dao;
	
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

    @Override
    public void generateAllDrugEvents(Date sinceWhen) {
        if(sinceWhen != null) {
            if(sinceWhen.compareTo(new Date())>0){
                throw new APIException("Date: "+sinceWhen+" should be greater than the date of today");
            }else{

            }
        }else{
            //All drug events.
        }
    }

    @Override
    public void generateDrugEventForPatient(Patient patient, Date sinceWhen) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generateDrugEventsFromTrigger(DrugEventTrigger trigger, Date sinceWhen) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generateDrugEventsFromTriggerForPatient(DrugEventTrigger trigger, Patient patient, Date sinceWhen) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public List<DrugEventTrigger> getAllDrugEventTriggers(Boolean includeRetired) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}