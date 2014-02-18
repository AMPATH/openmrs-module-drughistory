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

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Concept;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Drug Event Trigger used to generate Drug Events based on certain criteria
 */
public class DrugEventTrigger extends BaseOpenmrsMetadata implements Serializable {

	private static final long serialVersionUID = 129823929L;

	private Integer drugEventTriggerId;
	private Set<Concept> questions;
	private Set<Concept> answers;
	private Concept eventConcept;
	private Concept eventReason;
	private transient String customQuery;
	private DrugEventType eventType;

	@Override
	public Integer getId() {
		return getDrugEventTriggerId();
	}

	@Override
	public void setId(Integer drugEventTriggerId) {
		setDrugEventTriggerId(drugEventTriggerId);
	}

	public Integer getDrugEventTriggerId() {
		return drugEventTriggerId;
	}

	public void setDrugEventTriggerId(Integer drugEventTriggerId) {
		this.drugEventTriggerId = drugEventTriggerId;
	}

	public Set<Concept> getQuestions() {
		if (questions == null)
			questions = new HashSet<Concept>();
		return questions;
	}

	public void setQuestions(Set<Concept> questions) {
		this.questions = questions;
	}

	public Set<Concept> getAnswers() {
		if (answers == null)
			answers = new HashSet<Concept>();
		return answers;
	}

	public void setAnswers(Set<Concept> answers) {
		this.answers = answers;
	}

	public Concept getEventConcept() {
		return eventConcept;
	}

	public void setEventConcept(Concept eventConcept) {
		this.eventConcept = eventConcept;
	}

	public Concept getEventReason() {
		return eventReason;
	}

	public void setEventReason(Concept eventReason) {
		this.eventReason = eventReason;
	}

	public String getCustomQuery() {
		return customQuery;
	}

	public void setCustomQuery(String customQuery) {
		this.customQuery = customQuery;
	}

	public boolean hasCustomQuery() {
		return customQuery != null;
	}

	public DrugEventType getEventType() {
		return eventType;
	}

	public void setEventType(DrugEventType eventType) {
		this.eventType = eventType;
	}

	public void addQuestion(Concept concept) {
		this.getQuestions().add(concept);
	}

	public void addQuestions(Concept... concepts) {
		this.getQuestions().addAll(Arrays.asList(concepts));
	}

	public void addAnswer(Concept concept) {
		this.getAnswers().add(concept);
	}

	public void addAnswers(Concept... concepts) {
		this.getAnswers().addAll(Arrays.asList(concepts));
	}
}
