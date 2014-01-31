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

import java.io.Serializable;
import java.util.Date;

import org.openmrs.*;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class DrugEvent extends BaseOpenmrsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer drugEventId;
    private Person person;
    private Encounter encounter;
    private Concept concept;
    private Concept reason;
    private Date dateOccurred;
    private DrugEventType type;

    public DrugEvent(Person person, Concept concept, DrugEventType type) {
        this.person = person;
        this.concept = concept;
        this.type = type;
    }

    public DrugEvent(Person person, Concept concept, Date dateOccurred, DrugEventType type) {
        this.person = person;
        this.concept = concept;
        this.dateOccurred = dateOccurred;
        this.type = type;
    }

    public DrugEvent(Person person, Encounter encounter, Concept concept, Concept reason, Date dateOccurred, DrugEventType type) {
        this.person = person;
        this.encounter = encounter;
        this.concept = concept;
        this.reason = reason;
        this.dateOccurred = dateOccurred;
        this.type = type;
    }

    public Integer getDrugEventId() {
        return drugEventId;
    }

    public void setDrugEventId(Integer drugEventId) {
        this.drugEventId = drugEventId;
    }

    @Override
	public Integer getId() {
		return getDrugEventId();
	}
	
	@Override
	public void setId(Integer id) {
		setDrugEventId(id);
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

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public Date getDateOccurred() {
        return dateOccurred;
    }

    public void setDateOccurred(Date dateOccurred) {
        this.dateOccurred = dateOccurred;
    }

    public DrugEventType getType() {
        return type;
    }

    public void setType(DrugEventType type) {
        this.type = type;
    }

    public Concept getReason() {
        return reason;
    }

    public void setReason(Concept reason) {
        this.reason = reason;
    }
}