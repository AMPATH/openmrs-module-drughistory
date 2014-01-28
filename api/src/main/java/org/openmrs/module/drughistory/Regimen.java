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

import java.util.HashSet;
import java.util.Set;

public class Regimen extends BaseOpenmrsMetadata {

	public static final String LINE_FIRST = "FIRST";
	public static final String LINE_SECOND = "SECOND";
	public static final String AGE_ADULT = "ADULT";
	public static final String AGE_PEDS = "PEDS";

	private Integer regimenId = null;
	private String line = null;
	private String age = null;
	private Set<Concept> drugs = null;

	public Regimen() {
		// pass
	}

	public Regimen(String name, String description, String line, String age, Set<Concept> drugs) {
		this.setName(name);
		this.setDescription(description);
		this.setLine(line);
		this.setAge(age);
		this.setDrugs(drugs);
	}

	@Override
	public Integer getId() {
		return getRegimenId();
	}

	@Override
	public void setId(Integer id) {
		setRegimenId(id);
	}

	public Integer getRegimenId() {
		return regimenId;
	}

	public void setRegimenId(Integer regimenId) {
		this.regimenId = regimenId;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Set<Concept> getDrugs() {
		if (drugs == null) {
			drugs = new HashSet<Concept>();
		}
		return drugs;
	}

	public void setDrugs(Set<Concept> drugs) {
		this.drugs = drugs;
	}

	public void addDrug(Concept drug) {
		this.getDrugs().add(drug);
	}
}
