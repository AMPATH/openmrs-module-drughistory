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

import org.openmrs.Person;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 */
@Transactional
public interface DrugEventService extends OpenmrsService {
	/**
	 * Implementations of this method generate DrugEvent objects from a specific date passed as parameter to Date
	 * If date is null DrugEvent will be generates for the entire period of system existence.
	 *
	 * @param sinceWhen Date from which to calculate drug events
	 * @throws IllegalArgumentException
	 * @should throw IllegalArgumentException if sinceWhen is greater than today.
	 */
	@Transactional(readOnly = false)
	void generateAllDrugEvents(Date sinceWhen) throws IllegalArgumentException;

	/**
	 * Generate drug  events for a given a patient from a given date up to now.
	 *
	 * @param person
	 * @param sinceWhen
	 */
	@Transactional(readOnly = false)
	void generateAllDrugEvents(Person person, Date sinceWhen);

	/**
	 * Generate drug events from a trigger
	 *
	 * @param trigger
	 * @param sinceWhen
	 * @throws IllegalArgumentException
	 * @should throw exception when trigger is null
	 * @should throw exception when sinceWhen is in the future
	 * @should generate drug events with given concept question
	 * @should generate drug events with obs datetime later than or equal to sinceWhen
	 * @should fail when trigger event type is unspecified
	 */
	@Transactional(readOnly = false)
	void generateDrugEventsFromTrigger(DrugEventTrigger trigger, Date sinceWhen) throws IllegalArgumentException;

	/**
	 * @param trigger
	 * @param person
	 * @param sinceWhen
	 */
	@Transactional(readOnly = false)
	void generateDrugEventsFromTrigger(Person person, DrugEventTrigger trigger, Date sinceWhen) throws IllegalArgumentException;

	/**
	 * @param includeRetired
	 * @return List of event triggers.
	 */
	@Transactional(readOnly = true)
	List<DrugEventTrigger> getAllDrugEventTriggers(Boolean includeRetired);

	/**
	 * @param sinceWhen
	 * @return list of drug events since a given date if not null
	 */
	@Transactional(readOnly = true)
	List<DrugEvent> getAllDrugEvents(Date sinceWhen) throws IllegalArgumentException;

	/**
	 */
	@Transactional(readOnly = true)
	List<DrugEvent> getDrugEvents(Properties params) throws IllegalArgumentException;

	/**
	 * purges all drug events
	 */
	@Transactional(readOnly = false)
	void purgeAllDrugEvents();
}
