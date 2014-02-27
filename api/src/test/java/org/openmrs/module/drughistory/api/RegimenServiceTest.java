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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.drughistory.DrugSnapshot;
import org.openmrs.module.drughistory.Regimen;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.annotation.ExpectedException;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RegimenServiceTest extends BaseModuleContextSensitiveTest {
	/**
	 * @verifies not return retired regimens if includeRetired parameter is false
	 * @see RegimenService#getAllRegimens(Boolean)
	 */
	@Test
	public void getAllRegimens_shouldNotReturnRetiredRegimensIfIncludeRetiredParameterIsFalse() throws Exception {
		Context.getService(RegimenService.class).saveRegimen(
				new Regimen("A", "A Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Regimen r = Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Context.getService(RegimenService.class).retireRegimen(r, "for testing purposes");

		List<Regimen> actual = Context.getService(RegimenService.class).getAllRegimens(false);

		Assert.assertThat(actual.size(), is(1));
		Assert.assertThat(actual.get(0).getName(), is("A"));
		Assert.assertFalse(actual.contains(r));
	}

	/**
	 * @verifies not return retired regimens if includeRetired parameter is null
	 * @see RegimenService#getAllRegimens(Boolean)
	 */
	@Test
	public void getAllRegimens_shouldNotReturnRetiredRegimensIfIncludeRetiredParameterIsNull() throws Exception {
		Context.getService(RegimenService.class).saveRegimen(
				new Regimen("A", "A Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Regimen r = Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Context.getService(RegimenService.class).retireRegimen(r, "for testing purposes");

		List<Regimen> actual = Context.getService(RegimenService.class).getAllRegimens(null);

		Assert.assertThat(actual.size(), is(1));
		Assert.assertThat(actual.get(0).getName(), is("A"));
		Assert.assertFalse(actual.contains(r));
	}

	/**
	 * @verifies return retired regimens if includeRetired parameter is true
	 * @see RegimenService#getAllRegimens(Boolean)
	 */
	@Test
	public void getAllRegimens_shouldReturnRetiredRegimensIfIncludeRetiredParameterIsTrue() throws Exception {
		Context.getService(RegimenService.class).saveRegimen(
				new Regimen("A", "A Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Regimen r = Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Context.getService(RegimenService.class).retireRegimen(r, "for testing purposes");

		List<Regimen> actual = Context.getService(RegimenService.class).getAllRegimens(true);

		Assert.assertThat(actual.size(), is(2));
		Assert.assertTrue(actual.contains(r));
	}

	/**
	 * @verifies return a persisted regimen if found
	 * @see RegimenService#getRegimen(Integer)
	 */
	@Test
	public void getRegimen_shouldReturnAPersistedRegimenIfFound() throws Exception {
		Regimen expected = new Regimen("A", "A Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null);

		Assert.assertNull(expected.getId());

		Regimen actual = Context.getService(RegimenService.class).saveRegimen(expected);

		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());

		Integer id = actual.getId();

		Regimen actual2 = Context.getService(RegimenService.class).getRegimen(id);

		Assert.assertNotNull(actual2);
		Assert.assertNotNull(actual2.getId());
		Assert.assertThat(actual2.getId(), is(id));
		Assert.assertThat(actual2.getName(), is("A"));
	}

	/**
	 * @verifies return null if the persisted regimen is not found
	 * @see RegimenService#getRegimen(Integer)
	 */
	@Test
	public void getRegimen_shouldReturnNullIfThePersistedRegimenIsNotFound() throws Exception {
		Regimen actual = Context.getService(RegimenService.class).getRegimen(999);
		Assert.assertNull(actual);
	}

	/**
	 * @verifies persist a regimen that was not already persisted
	 * @see RegimenService#saveRegimen(org.openmrs.module.drughistory.Regimen)
	 */
	@Test
	public void saveRegimen_shouldPersistARegimenThatWasNotAlreadyPersisted() throws Exception {
		Regimen expected = new Regimen("A", "A Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null);
		expected.addDrug(Context.getConceptService().getConcept(3));
		expected.addDrug(Context.getConceptService().getConcept(4));

		Assert.assertNull(expected.getId());

		Regimen actual = Context.getService(RegimenService.class).saveRegimen(expected);

		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		Assert.assertThat(actual.getName(), is("A"));
		Assert.assertThat(actual.getDrugs().size(), is(2));
	}

	/**
	 * @verifies update a regimen that was previously persisted
	 * @see RegimenService#saveRegimen(org.openmrs.module.drughistory.Regimen)
	 */
	@Test
	public void saveRegimen_shouldUpdateARegimenThatWasPreviouslyPersisted() throws Exception {
		Regimen expected = new Regimen("A", "A Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null);

		Assert.assertNull(expected.getId());

		Regimen actual = Context.getService(RegimenService.class).saveRegimen(expected);

		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		Assert.assertThat(actual.getName(), is("A"));

		actual.setName("B");

		Regimen actual2 = Context.getService(RegimenService.class).saveRegimen(actual);

		Assert.assertNotNull(actual2);
		Assert.assertNotNull(actual2.getId());
		Assert.assertThat(actual2.getName(), is("B"));
	}

	/**
	 * @verifies throw an APIException if regimen is null
	 * @see RegimenService#saveRegimen(org.openmrs.module.drughistory.Regimen)
	 */
	@Test
	@ExpectedException(APIException.class)
	public void saveRegimen_shouldThrowAnAPIExceptionIfRegimenIsNull() throws Exception {
		Context.getService(RegimenService.class).saveRegimen(null);
	}

	/**
	 * @verifies set retire related fields appropriately
	 * @see RegimenService#retireRegimen(org.openmrs.module.drughistory.Regimen, String)
	 */
	@Test
	public void retireRegimen_shouldSetRetireRelatedFieldsAppropriately() throws Exception {
		Regimen r = Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Context.getService(RegimenService.class).retireRegimen(r, "for testing purposes");

		Assert.assertTrue(r.isRetired());
		Assert.assertThat(r.getRetiredBy(), is(Context.getAuthenticatedUser()));
		Assert.assertThat(r.getRetireReason(), is("for testing purposes"));
		Assert.assertNotNull(r.getDateRetired());
	}

	/**
	 * @verifies throw an APIException if regimen is null
	 * @see RegimenService#retireRegimen(org.openmrs.module.drughistory.Regimen, String)
	 */
	@Test
	@ExpectedException(APIException.class)
	public void retireRegimen_shouldThrowAnAPIExceptionIfRegimenIsNull() throws Exception {
		Context.getService(RegimenService.class).retireRegimen(null, "for testing purposes");
	}

	/**
	 * @verifies throw an APIException if retireReason is null
	 * @see RegimenService#retireRegimen(org.openmrs.module.drughistory.Regimen, String)
	 */
	@Test
	@ExpectedException(APIException.class)
	public void retireRegimen_shouldThrowAnAPIExceptionIfRetireReasonIsNull() throws Exception {
		Regimen r = Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));
		Context.getService(RegimenService.class).retireRegimen(r, null);
	}

	/**
	 * @verifies throw an APIException if retireReason is empty
	 * @see RegimenService#retireRegimen(org.openmrs.module.drughistory.Regimen, String)
	 */
	@Test
	@ExpectedException(APIException.class)
	public void retireRegimen_shouldThrowAnAPIExceptionIfRetireReasonIsEmpty() throws Exception {
		Regimen r = Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));
		Context.getService(RegimenService.class).retireRegimen(r, "");
	}

	/**
	 * @verifies unset retire related fields appropriately
	 * @see RegimenService#unretireRegimen(org.openmrs.module.drughistory.Regimen)
	 */
	@Test
	public void unretireRegimen_shouldUnsetRetireRelatedFieldsAppropriately() throws Exception {
		Regimen r = Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null));

		Context.getService(RegimenService.class).retireRegimen(r, "for testing purposes");

		Assert.assertTrue(r.isRetired());
		Assert.assertThat(r.getRetiredBy(), is(Context.getAuthenticatedUser()));
		Assert.assertThat(r.getRetireReason(), is("for testing purposes"));
		Assert.assertThat(r.getDateRetired(), is(new Date()));

		Context.getService(RegimenService.class).unretireRegimen(r);

		Assert.assertFalse(r.isRetired());
		Assert.assertNull(r.getRetiredBy());
		Assert.assertNull(r.getRetireReason());
		Assert.assertNull(r.getDateRetired());
	}

	/**
	 * @verifies purge a regimen from the database
	 * @see RegimenService#purgeRegimen(org.openmrs.module.drughistory.Regimen)
	 */
	@Test
	public void purgeRegimen_shouldPurgeARegimenFromTheDatabase() throws Exception {
		Regimen expected = new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, null);
		expected.addDrug(Context.getConceptService().getConcept(3));
		expected.addDrug(Context.getConceptService().getConcept(4));

		Regimen r = Context.getService(RegimenService.class).saveRegimen(expected);

		Assert.assertNotNull(r);
		Assert.assertNotNull(r.getId());

		Integer id = r.getId();

		Context.getService(RegimenService.class).purgeRegimen(r);

		Regimen actual = Context.getService(RegimenService.class).getRegimen(id);

		Assert.assertNull(actual);
	}

	/**
	 * @verifies throw an APIException if regimen is null
	 * @see RegimenService#purgeRegimen(org.openmrs.module.drughistory.Regimen)
	 */
	@Test
	@ExpectedException(APIException.class)
	public void purgeRegimen_shouldThrowAnAPIExceptionIfRegimenIsNull() throws Exception {
		Context.getService(RegimenService.class).purgeRegimen(null);
	}

	/**
	 * @verifies only return regimens matching drugs in the snapshot
	 * @see RegimenService#getRegimensFromSnapshot(org.openmrs.module.drughistory.DrugSnapshot)
	 */
	@Test
	public void getRegimensFromSnapshot_shouldOnlyReturnRegimensMatchingDrugsInTheSnapshot() throws Exception {
		Set<Concept> drugs = new HashSet<Concept>();
		drugs.add(Context.getConceptService().getConcept(3));
		drugs.add(Context.getConceptService().getConcept(88));
		drugs.add(Context.getConceptService().getConcept(792));

		Context.getService(RegimenService.class).saveRegimen(
				new Regimen("A", "A Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, new HashSet<Concept>(drugs)));

		drugs.clear();
		drugs.add(Context.getConceptService().getConcept(11));
		drugs.add(Context.getConceptService().getConcept(12));
		drugs.add(Context.getConceptService().getConcept(13));

		Context.getService(RegimenService.class).saveRegimen(
				new Regimen("B", "B Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, new HashSet<Concept>(drugs)));

		drugs.clear();
		drugs.add(Context.getConceptService().getConcept(3));
		drugs.add(Context.getConceptService().getConcept(88));
		drugs.add(Context.getConceptService().getConcept(13));

		Context.getService(RegimenService.class).saveRegimen(
				new Regimen("C", "C Desc", Regimen.LINE_FIRST, Regimen.AGE_ADULT, new HashSet<Concept>(drugs)));

		DrugSnapshot ds = new DrugSnapshot();
		ds.setConcepts(new HashSet<Concept>(drugs));

		List<Regimen> actual = Context.getService(RegimenService.class).getRegimensFromSnapshot(ds);

		assertNotNull(actual);
		assertEquals(1, actual.size());
		assertEquals("C", actual.get(0).getName());
	}
}
