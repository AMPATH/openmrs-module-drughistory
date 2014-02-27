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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.DrugEventType;
import org.openmrs.module.drughistory.DrugSnapshot;
import org.openmrs.module.drughistory.api.DrugEventService;
import org.openmrs.module.drughistory.api.DrugSnapshotService;
import org.openmrs.module.drughistory.api.db.DrugSnapshotDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class DrugSnapshotServiceImpl extends BaseOpenmrsService implements DrugSnapshotService {

	protected final Log log = LogFactory.getLog(this.getClass());

	private DrugSnapshotDAO dao;

	public void setDao(DrugSnapshotDAO dao) {
		this.dao = dao;
	}

	@Override
	public void generateDrugSnapshots(Date sinceWhen) {
		generateDrugSnapshots(null, sinceWhen);
	}

	/**
	 * Plow through drug events and create snapshots from them
	 * TODO find a way to batch or use a cursor
	 */
	@Override
	public void generateDrugSnapshots(Patient patient, Date sinceWhen) {
		// set up params for drug event query
		Properties params = new Properties();
		if (patient != null) {
			params.put("person", patient);
		}
		if (sinceWhen != null) {
			params.put("since", sinceWhen);
		}

		// get the drug events
		List<DrugEvent> events = Context.getService(DrugEventService.class).getDrugEvents(params);

		// order events by person and date
		Map<Person, SortedMap<Date, List<DrugEvent>>> byPersonAndDate = groupByPersonAndDate(events);

		// create snapshots and save after processing each person
		List<DrugSnapshot> snapshots = new ArrayList<DrugSnapshot>();
		for (Person p : byPersonAndDate.keySet()) {

			DrugSnapshot s = new DrugSnapshot();
			s.setPerson(p);

			// for each date, calculate the new set of relevant concepts
			SortedMap<Date, List<DrugEvent>> dateListMap = byPersonAndDate.get(p);

			for (Date d : dateListMap.keySet()) {
				s.setDateTaken(d);
				for (DrugEvent de : dateListMap.get(d)) {
					if (de.getEventType() == DrugEventType.START || de.getEventType() == DrugEventType.CONTINUE) {
						s.addConcept(de.getConcept());
					} else if (de.getEventType() == DrugEventType.STOP) {
						s.removeConcept(de.getConcept());
					}

					// the snapshot's encounter will be the one from the last-added observation
					s.setEncounter(de.getEncounter());
				}

				// add a copy to snapshots, since we want to reuse this one
				DrugSnapshot ds = s.copy();

				// ... and give it a UUID
				ds.setUuid(UUID.randomUUID().toString());

				snapshots.add(ds);
			}

			// save and clear snapshots
			saveSnapshots(snapshots);
			snapshots.clear();

			// flush the context so we don't run into memory issues
			Context.flushSession();
			Context.clearSession();
		}
	}

	private void saveSnapshots(List<DrugSnapshot> snapshots) {
		dao.saveSnapshots(snapshots);
	}

	private Map<Person, SortedMap<Date, List<DrugEvent>>> groupByPersonAndDate(List<DrugEvent> events) {
		Map<Person, SortedMap<Date, List<DrugEvent>>> m = new HashMap<Person, SortedMap<Date, List<DrugEvent>>>();
		for (DrugEvent de : events) {
			if (!m.containsKey(de.getPerson())) {
				m.put(de.getPerson(), new TreeMap<Date, List<DrugEvent>>());
			}
			SortedMap<Date, List<DrugEvent>> mm = m.get(de.getPerson());
			if (!mm.containsKey(de.getDateOccurred())) {
				mm.put(de.getDateOccurred(), new ArrayList<DrugEvent>());
			}
			mm.get(de.getDateOccurred()).add(de);
		}
		return m;
	}

	@Override
	public List<DrugSnapshot> getDrugSnapshots(Properties properties) {
		return dao.getDrugSnapshots(properties);
	}
}
