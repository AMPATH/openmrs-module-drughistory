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

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.drughistory.DrugSnapshot;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DrugSnapshotServiceTest extends BaseModuleContextSensitiveTest {

	@Before
	public void setUp() throws Exception {
		executeDataSet("datasets/drugevents-drugsnapshotservice.xml");
	}

	/**
	 * @verifies generate snapshots for everyone if no patient specified
	 * @see DrugSnapshotService#generateDrugSnapshots(org.openmrs.Patient, java.util.Date)
	 */
	@Test
	public void generateDrugSnapshots_shouldGenerateSnapshotsForEveryoneIfNoPatientSpecified() throws Exception {
		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(null, null);
		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(null);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		Set<Person> people = new HashSet<Person>();
		for (DrugSnapshot ds : actual) {
			people.add(ds.getPerson());
		}
		assertTrue(people.size() > 1);
	}

	/**
	 * @verifies generate snapshots for all time if no sinceWhen specified
	 * @see DrugSnapshotService#generateDrugSnapshots(org.openmrs.Patient, java.util.Date)
	 */
	@Test
	public void generateDrugSnapshots_shouldGenerateSnapshotsForAllTimeIfNoSinceWhenSpecified() throws Exception {
		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(null, null);
		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(null);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		Date min = new Date();
		for (DrugSnapshot ds : actual) {
			min = ds.getDateTaken().before(min) ? ds.getDateTaken() : min;
		}

		assertEquals(makeDate("16 Oct 1975"), min);
	}

	/**
	 * @verifies generate snapshots for just one patient if specified
	 * @see DrugSnapshotService#generateDrugSnapshots(org.openmrs.Patient, java.util.Date)
	 */
	@Test
	public void generateDrugSnapshots_shouldGenerateSnapshotsForJustOnePatientIfSpecified() throws Exception {
		Patient p = Context.getPatientService().getPatient(2);

		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(p, null);
		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(null);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		Set<Person> people = new HashSet<Person>();
		for (DrugSnapshot ds : actual) {
			people.add(ds.getPerson());
		}

		assertEquals(1, people.size());
		assertEquals(p, actual.get(0).getPerson());
	}

	/**
	 * @verifies generate snapshots for just occurrences since date specified
	 * @see DrugSnapshotService#generateDrugSnapshots(org.openmrs.Patient, java.util.Date)
	 */
	@Test
	public void generateDrugSnapshots_shouldGenerateSnapshotsForJustOccurrencesSinceDateSpecified() throws Exception {
		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(null, makeDate("17 Oct 1975"));
		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(null);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		Date min = new Date();
		for (DrugSnapshot ds : actual) {
			min = ds.getDateTaken().before(min) ? ds.getDateTaken() : min;
		}

		assertEquals(makeDate("17 Oct 1975"), min);
	}

	/**
	 * @verifies add concepts to snapshots based on DrugEventType START
	 * @see DrugSnapshotService#generateDrugSnapshots(org.openmrs.Patient, java.util.Date)
	 */
	@Test
	public void generateDrugSnapshots_shouldAddConceptsToSnapshotsBasedOnDrugEventTypeSTART() throws Exception {
		Patient p = Context.getPatientService().getPatient(2);

		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(p, null);
		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(null);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		assertEquals(makeDate("16 Oct 1975"), actual.get(0).getDateTaken());
		assertTrue(actual.get(0).getConcepts().contains(Context.getConceptService().getConcept(792)));
		assertTrue(actual.get(0).getConcepts().contains(Context.getConceptService().getConcept(88)));
	}

	/**
	 * @verifies add concepts to snapshots based on DrugEventType CONTINUE
	 * @see DrugSnapshotService#generateDrugSnapshots(org.openmrs.Patient, java.util.Date)
	 */
	@Test
	public void generateDrugSnapshots_shouldAddConceptsToSnapshotsBasedOnDrugEventTypeCONTINUE() throws Exception {
		Patient p = Context.getPatientService().getPatient(1);

		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(p, null);
		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(null);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		assertEquals(makeDate("17 Oct 1975"), actual.get(0).getDateTaken());
		assertTrue(actual.get(0).getConcepts().contains(Context.getConceptService().getConcept(792)));
	}

	/**
	 * @verifies remove concepts from snapshots based on DrugEventType STOP
	 * @see DrugSnapshotService#generateDrugSnapshots(org.openmrs.Patient, java.util.Date)
	 */
	@Test
	public void generateDrugSnapshots_shouldRemoveConceptsFromSnapshotsBasedOnDrugEventTypeSTOP() throws Exception {
		Patient p = Context.getPatientService().getPatient(2);

		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(p, null);
		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(null);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		assertEquals(makeDate("17 Oct 1975"), actual.get(1).getDateTaken());
		assertFalse(actual.get(1).getConcepts().contains(Context.getConceptService().getConcept(88)));

		assertEquals(makeDate("18 Oct 1975"), actual.get(2).getDateTaken());
		assertFalse(actual.get(2).getConcepts().contains(Context.getConceptService().getConcept(792)));
	}

	@Test
	public void getDrugSnapshots_testParams() throws Exception {
		Context.getService(DrugSnapshotService.class).generateDrugSnapshots(null, null);

		// test drugs param

		Concept c = Context.getConceptService().getConcept(792);
		Set<Integer> drugs = new HashSet<Integer>();
		drugs.add(792);

		Properties params = new Properties();
		params.put("drugs", drugs);

		List<DrugSnapshot> actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(params);

		assertNotNull(actual);
		assertFalse(actual.isEmpty());

		for (DrugSnapshot ds : actual) {
			assertTrue(ds.getConcepts().contains(c));
		}

//		// test atLeast param
//
//		params = new Properties();
//		params.put("atLeast", 2);
//
//		actual = Context.getService(DrugSnapshotService.class).getDrugSnapshots(params);
//
//		assertNotNull(actual);
//		assertFalse(actual.isEmpty());
//
//		for (DrugSnapshot ds : actual) {
//			assertTrue(ds.getConcepts().size() >= 2);
//		}
	}

	private Date makeDate(String date) {
		try {
			return new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).parse(date);
		} catch (ParseException e) {
			// pass
		}
		return new Date();
	}
}
