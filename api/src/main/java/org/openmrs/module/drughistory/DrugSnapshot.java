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

package org.openmrs.module.drughistory;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Person;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DrugSnapshot extends BaseOpenmrsObject {

	private Integer drugSnapshotId;
	private Person person;
	private Encounter encounter;
	private Set<Concept> concepts;
	private Date dateTaken;

	@Override
	public Integer getId() {
		return getDrugSnapshotId();
	}

	@Override
	public void setId(Integer id) {
		setDrugSnapshotId(id);
	}

	public Integer getDrugSnapshotId() {
		return drugSnapshotId;
	}

	public void setDrugSnapshotId(Integer drugSnapshotId) {
		this.drugSnapshotId = drugSnapshotId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

	public Set<Concept> getConcepts() {
		if (concepts == null)
			concepts = new HashSet<Concept>();
		return concepts;
	}

	public void addConcept(Concept concept) {
		getConcepts().add(concept);
	}

	public void setConcepts(Set<Concept> concepts) {
		this.concepts = concepts;
	}

	public Date getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}

	public void removeConcept(Concept concept) {
		getConcepts().remove(concept);
	}

	public DrugSnapshot copy() {
		DrugSnapshot ds = new DrugSnapshot();
		ds.setDateTaken(this.getDateTaken());
		ds.setPerson(this.getPerson());
		ds.setEncounter(this.getEncounter());
		for (Concept c : this.getConcepts()) {
			ds.addConcept(c);
		}
		return ds;
	}
}
