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
package org.openmrs.module.drughistory.api.db;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.openmrs.module.drughistory.api.DrugEventService;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link DrugEventService}.
 */
public interface DrugEventDAO {
    /**
     * saves a newly created DrugEvent.
     * @param drugEvent
     */
    void saveDrugEvent(DrugEvent drugEvent) throws DAOException;

    /**
     * saveDrugEvents is used to save multiple drug in a batch
     * @param drugEvents  list of drug events to be saved
     * @throws DAOException
     */
    void saveDrugEvents(List<DrugEvent> drugEvents,int bathSize) throws DAOException;

    /**
     *
     * @param person  a patient for whom drug events are required
     * @return list of DrugEvent belonging to person for the entire period
     * @throws DAOException
     */
    List<DrugEvent> getDrugEventsForPatient(Person person) throws DAOException;

    /**
     *
     * @param person    a patient for whom drug events are required
     * @param sinceWhen a date from which drug events are required
     * @return list of DrugEvent belonging to person for period between sinceWhen and now
     * @throws DAOException
     */
    List<DrugEvent> getDrugEventsForPatient(Person person, Date sinceWhen) throws DAOException;

    /**
     *
     * @param encounter
     * @return   list of DrugEvents for a given encounter
     * @throws DAOException
     */
    List<DrugEvent> getEncounterDrugEvents(Encounter encounter)throws DAOException;

    /**
     * Purges a drug event passed as parameter
     * @param drugEvent a drug event to be purged
     * @throws DAOException
     */
    void purgeDrugEvent(DrugEvent drugEvent) throws DAOException;

    /**
     * Purge all drug events in the system
     * @return the number of Drug event rows deleted
     * @throws DAOException
     */
    int purgeAllDrugEvents() throws DAOException;

    /**
     * Purge all drug events relating to patient
     * @param person a patient whose drug events have to be purged.
     * @return the number of drug event rows belonging to person that have been deleted.
     * @throws DAOException
     */
    int purgeDrugEventsForPatient(Person person) throws DAOException;

    /**
     *
     * @param trigger
     * @param sinceWhen
     */
    void generateDrugEventsFromTrigger(DrugEventTrigger trigger, Date sinceWhen);

    /**
     *
     * @param person
     * @param trigger
     * @param sinceWhen
     */
    void generateDrugEventsFromTrigger(Person person,DrugEventTrigger trigger,Date sinceWhen);

    /**
     *
     * @param sinceWhen
     * @return list of DrugEvents since a given date if not null
     */
    List<DrugEvent> getAllDrugEvents(Date sinceWhen);
}