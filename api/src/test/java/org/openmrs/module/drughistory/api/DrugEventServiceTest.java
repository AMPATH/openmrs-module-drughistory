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

import static org.junit.Assert.*;
import org.junit.Test;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.annotation.ExpectedException;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Tests {@link ${DrugEventService}}.
 */
public class  DrugEventServiceTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void shouldSetupContext() {
		assertNotNull(Context.getService(DrugEventService.class));
	}

    @Test
    @ExpectedException(APIException.class)
    public void generateAllDrugEvents_shouldGenerateAPIExeptionIfSinceWhenIsGreaterThanToday() throws Exception{
        GregorianCalendar gc = new GregorianCalendar(2014,2,14);
        Date sinceWhen = gc.getTime();
        Context.getService(DrugEventService.class).generateAllDrugEvents(sinceWhen);
    }
}
