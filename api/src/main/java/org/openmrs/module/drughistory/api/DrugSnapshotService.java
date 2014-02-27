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

import org.openmrs.Patient;
import org.openmrs.module.drughistory.DrugSnapshot;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Properties;

@Transactional
public interface DrugSnapshotService {

	@Transactional(readOnly = false)
	public void generateDrugSnapshots(Date sinceWhen);

	/**
	 * generates drug snapshots
	 * @should generate snapshots for everyone if no patient specified
	 * @should generate snapshots for all time if no sinceWhen specified
	 * @should generate snapshots for just one patient if specified
	 * @should generate snapshots for just occurrences since date specified
	 * @should add concepts to snapshots based on DrugEventType START
	 * @should add concepts to snapshots based on DrugEventType CONTINUE
	 * @should remove concepts from snapshots based on DrugEventType STOP
	 */
	@Transactional(readOnly = false)
	public void generateDrugSnapshots(Patient patient, Date sinceWhen);

	@Transactional(readOnly = true)
	public List<DrugSnapshot> getDrugSnapshots(Properties params);
}
