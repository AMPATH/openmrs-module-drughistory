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

import org.openmrs.module.drughistory.Regimen;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface RegimenService {

	/**
	 * Returns all persisted regimens, with the option to include retired objects.  By default,
	 * only non-retired objects are returned unless specified.
	 *
	 * @param includeRetired specifies whether to include retired regimens in the return data
	 * @return a list of persisted regimens
	 * @should not return retired regimens if includeRetired parameter is false
	 * @should not return retired regimens if includeRetired parameter is null
	 * @should return retired regimens if includeRetired parameter is true
	 */
	@Transactional(readOnly = true)
	public List<Regimen> getAllRegimens(Boolean includeRetired);

	/**
	 * Returns a single persisted regimen, based on its database identifier.
	 *
	 * @param regimenId the database identifier for the desired regimen
	 * @return the persisted regimen
	 * @should return a persisted regimen if found
	 * @should return null if the persisted regimen is not found
	 */
	@Transactional(readOnly = false)
	public Regimen getRegimen(Integer regimenId);

	/**
	 * Persists a regimen to the database.  If the regimen already exists, it is updated.
	 *
	 * @param regimen the regimen to be persisted
	 * @return the persisted regimen
	 * @should persist a regimen that was not already persisted
	 * @should update a regimen that was previously persisted
	 * @should throw an APIException if regimen is null
	 */
	@Transactional(readOnly = false)
	public Regimen saveRegimen(Regimen regimen);

	/**
	 * Retires a regimen by setting the appropriate values on the persisted object.
	 *
	 * @param regimen the regimen to be retired
	 * @param retireReason the reason for retiring the regimen
	 * @should set retire related fields appropriately
	 * @should throw an APIException if regimen is null
	 * @should throw an APIException if retireReason is null
	 * @should throw an APIException if retireReason is empty
	 */
	@Transactional(readOnly = false)
	public void retireRegimen(Regimen regimen, String retireReason);

	/**
	 * Un-retires a regimen by un-setting the relevant values on the persisted object.
	 *
	 * @param regimen the regimen to be un-retired
	 * @should unset retire related fields appropriately
	 */
	@Transactional(readOnly = false)
	public void unretireRegimen(Regimen regimen);

	/**
	 * Purges a regimen from persistence.
	 *
	 * @param regimen the regimen to be purged
	 * @should purge a regimen from the database
	 * @should throw an APIException if regimen is null
	 */
	@Transactional(readOnly = false)
	public void purgeRegimen(Regimen regimen);
}
