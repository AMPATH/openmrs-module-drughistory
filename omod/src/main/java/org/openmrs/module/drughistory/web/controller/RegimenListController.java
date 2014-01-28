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

package org.openmrs.module.drughistory.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.drughistory.Regimen;
import org.openmrs.module.drughistory.api.RegimenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("module/drughistory/regimen.list")
public class RegimenListController {

	@ModelAttribute("regimens")
	public List<Regimen> getAllRegimens() {
		return Context.getService(RegimenService.class).getAllRegimens(true);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showARTRegimens() {
		return "module/drughistory/regimenList";
	}


}
