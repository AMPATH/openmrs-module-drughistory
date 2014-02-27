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
import org.openmrs.Concept;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.drughistory.DrugSnapshot;
import org.openmrs.module.drughistory.Regimen;
import org.openmrs.module.drughistory.api.RegimenService;
import org.openmrs.module.drughistory.api.db.RegimenDAO;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class RegimenServiceImpl extends BaseOpenmrsService implements RegimenService {

	RegimenDAO dao;
	private final Log log = LogFactory.getLog(getClass());

	public RegimenDAO getDao() {
		return dao;
	}

	public void setDao(RegimenDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<Regimen> getAllRegimens(Boolean includeRetired) {
		return dao.getAllRegimens(includeRetired);
	}

	@Override
	public Regimen getRegimen(Integer artRegimenId) {
		return dao.getRegimen(artRegimenId);
	}

	@Override
	public List<Regimen> getRegimensFromSnapshot(DrugSnapshot snapshot) {
		Properties params = new Properties();
		params.put("drugs", snapshot.getConcepts());
		return dao.getRegimens(params);
	}

	@Override
	public Regimen saveRegimen(Regimen regimen) {
		if (regimen == null) {
			throw new APIException("A regimen must not be null to be saved.");
		}

		return dao.saveRegimen(regimen);
	}

	@Override
	public void retireRegimen(Regimen regimen, String retireReason) {
		if (regimen == null) {
			throw new APIException("A regimen must not be null to be retired.");
		}

		if (retireReason == null || retireReason.length() < 1) {
			throw new APIException("A reason is required when retiring a regimen.");
		}

		regimen.setRetired(true);
		regimen.setRetiredBy(Context.getAuthenticatedUser());
		regimen.setDateRetired(new Date());
		regimen.setRetireReason(retireReason);

		dao.saveRegimen(regimen);
	}

	@Override
	public void unretireRegimen(Regimen regimen) {
		if (regimen == null) {
			throw new APIException("A regimen must not be null to be unretired.");
		}

		regimen.setRetired(false);
		regimen.setRetiredBy(null);
		regimen.setDateRetired(null);
		regimen.setRetireReason(null);

		dao.saveRegimen(regimen);
	}

	@Override
	public void purgeRegimen(Regimen regimen) {
		if (regimen == null) {
			throw new APIException("A regimen must not be null to be purged.");
		}

		// empty the drug set before attempting to purge
		regimen.setDrugs(new HashSet<Concept>());
		regimen = dao.saveRegimen(regimen);

		dao.purgeRegimen(regimen);
	}
}
