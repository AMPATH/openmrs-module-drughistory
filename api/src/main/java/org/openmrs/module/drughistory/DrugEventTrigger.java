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

/**
 * Created with IntelliJ IDEA.
 * User: willa
 * Date: 1/29/14
 * Time: 3:31 PM
 */
public class DrugEventTrigger extends BaseOpenmrsMetadata implements Serializable{
    private static final long serialVersionUID = 129823929L;

    private Integer drugEventTriggerId;
    private Concept question;
    private Concept answer;
    private Concept eventConcept;
    private Concept eventReason;
    private transient String customQuery;

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

    public Concept getQuestion() {
        return question;
    }

    public void setQuestion(Concept question) {
        this.question = question;
    }

    public Concept getAnswer() {
        return answer;
    }

    public void setAnswer(Concept answer) {
        this.answer = answer;
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
}
