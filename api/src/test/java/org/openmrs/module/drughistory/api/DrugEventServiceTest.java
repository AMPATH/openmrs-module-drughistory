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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.drughistory.DrugEvent;
import org.openmrs.module.drughistory.DrugEventTrigger;
import org.openmrs.module.drughistory.DrugEventType;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.annotation.ExpectedException;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests {@link ${DrugEventService}}.
 */
public class  DrugEventServiceTest extends BaseModuleContextSensitiveTest {
    private DrugEventService drugEventService;

    @Before
    public void setUp() throws Exception{
        drugEventService = Context.getService(DrugEventService.class);
    }
	
	@Test
	public void shouldSetupContext() {
		assertNotNull(Context.getService(DrugEventService.class));
	}

    @Test
    @ExpectedException(IllegalArgumentException.class)
    public void generateAllDrugEvents_shouldThrowExceptionIfSinceWhenIsGreaterThanToday() throws Exception{
        GregorianCalendar gc = new GregorianCalendar(2014,2,14);
        Date sinceWhen = gc.getTime();
        Context.getService(DrugEventService.class).generateAllDrugEvents(sinceWhen);
    }

    /**
     * @verifies throw exception when trigger is null
     * @see DrugEventService#generateDrugEventsFromTrigger(org.openmrs.module.drughistory.DrugEventTrigger, java.util.Date)
     */
    @Test
    @ExpectedException(IllegalArgumentException.class)
    public void generateDrugEventsFromTrigger_shouldThrowExceptionWhenTriggerIsNull() throws Exception {
         drugEventService.generateDrugEventsFromTrigger(null,new Date());
    }

    /**
     * @verifies fail when trigger event type is unspecified
     * @see DrugEventService#generateDrugEventsFromTrigger(org.openmrs.module.drughistory.DrugEventTrigger, java.util.Date)
     */
    @Test
    @ExpectedException(IllegalArgumentException.class)
    public void generateDrugEventsFromTrigger_shouldFailWhenTriggerEventTypeIsUnspecified() throws Exception {
        DrugEventTrigger trigger = new DrugEventTrigger();
        drugEventService.generateDrugEventsFromTrigger(trigger,null);
    }

    /**
     * @verifies throw exception when sinceWhen is in the future
     * @see DrugEventService#generateDrugEventsFromTrigger(org.openmrs.module.drughistory.DrugEventTrigger, java.util.Date)
     */
    @Test
    @ExpectedException(IllegalArgumentException.class)
    public void generateDrugEventsFromTrigger_shouldThrowExceptionWhenSinceWhenIsInTheFuture() throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.DAY_OF_MONTH,1);
        AtomicReference<Date> sinceWhen = new AtomicReference<Date>();
        sinceWhen.set(gc.getTime());
        DrugEventTrigger trigger = new DrugEventTrigger();
        drugEventService.generateDrugEventsFromTrigger(trigger, sinceWhen.get());
    }

    /**
     * @verifies generate drug events with given concept question
     * @see DrugEventService#generateDrugEventsFromTrigger(org.openmrs.module.drughistory.DrugEventTrigger, java.util.Date)
     */
    @Test
    public void generateDrugEventsFromTrigger_shouldGenerateDrugEventsWithGivenConceptQuestion() throws Exception {
        DrugEventTrigger trigger = new DrugEventTrigger();
        trigger.setQuestion(Context.getConceptService().getConcept(5089));
        trigger.setEventConcept(trigger.getQuestion());
        trigger.setEventType(DrugEventType.START);
        drugEventService.generateDrugEventsFromTrigger(trigger,null);
        List<DrugEvent> drugEvents = drugEventService.getAllDrugEvents(null);
        //There are three obs with concept_id 5089
        Assert.assertEquals(drugEvents.size(), 3);

        DrugEvent event = drugEvents.get(0);
        Assert.assertEquals((long) event.getPerson().getPersonId(), 7);
        Assert.assertEquals((long) event.getConcept().getConceptId(),5089);
    }

    /**
     * @verifies generate drug events with obs datetime later than or equal to sinceWhen
     * @see DrugEventService#generateDrugEventsFromTrigger(org.openmrs.module.drughistory.DrugEventTrigger, java.util.Date)
     */
    @Test
    public void generateDrugEventsFromTrigger_shouldGenerateDrugEventsWithObsDatetimeLaterThanOrEqualToSinceWhen() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

}
