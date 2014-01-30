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
package org.openmrs.module.drughistory.api;

import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(DrugEventService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface DrugEventService extends OpenmrsService {
    /**
     * Implementations of this method generate DrugEvent objects from a specific date passed as parameter to Date
     * If date is null DrugEvent will be generates for the entire period of system existence.
     * @param sinceWhen Date from which to calculate drug events
     * @should check sinceWhen is earlier than or equal to today when not null
     * @should generate All drug events given start when sinceWhen not null
     * @should generate All drug events when sinceWhen is null
     */
    void generateAllDrugEvents(Date sinceWhen);

    /**
     * Generate drug  events for a given a patient from a given date up to now.
     * @param patient
     * @param sinceWhen
     * @should check patient is not null
     * @should generate patient's all drug events when sinceWhen is null
     * @should check sinceWhen is earlier than or equal to today when not null
     * @should generate patient's all drug events from a given date when sinceWhen is not null
     */
    void generateDrugEventForPatient(Patient patient,Date sinceWhen);

    /**
     * Generate drug events from a trigger
     * @param trigger
     * @param sinceWhen
     */
    void generateDrugEventsFromTrigger(DrugEventTrigger trigger, Date sinceWhen);

    /**
     *
     * @param trigger
     * @param patient
     * @param sinceWhen
     */
    void generateDrugEventsFromTriggerForPatient(DrugEventTrigger trigger, Patient patient, Date sinceWhen);

    /**
     *
     * @param includeRetired
     * @return   List of event triggers.
     */
    List<DrugEventTrigger> getAllDrugEventTriggers(Boolean includeRetired);
}