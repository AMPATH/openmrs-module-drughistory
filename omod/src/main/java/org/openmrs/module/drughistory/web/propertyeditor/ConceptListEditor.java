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
package org.openmrs.module.drughistory.web.propertyeditor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

/**
 * Handles lists of drugIds
 */
public class ConceptListEditor extends PropertyEditorSupport {

	private Log log = LogFactory.getLog(this.getClass());

	private Collection<Concept> originalConcepts = null;

	/**
	 * Default constructor taking in the original answers. This should be the actual list on the
	 * pojo object to prevent hibernate errors later on.
	 *
	 * @param originalConcepts the list on the pojo
	 */
	public ConceptListEditor(Collection<Concept> originalConcepts) {
		if (originalConcepts == null)
			this.originalConcepts = new HashSet<Concept>();
		else
			this.originalConcepts = originalConcepts;
	}

	/**
	 * loops over the textbox assigned to this property. The textbox is assumed to be a string of
	 * conceptIds^drugIds separated by spaces.
	 *
	 * @param text list of conceptIds (not conceptAnswerIds)
	 * @should set the sort weights with the least possible changes
	 */
	public void setAsText(String text) throws IllegalArgumentException {

		if (StringUtils.hasText(text)) {
			ConceptService cs = Context.getConceptService();
			String[] conceptIds = text.split(" ");
			List<String> requestConceptIds = new Vector<String>();
			//set up parameter answer Set for easier add/delete functions and removal of duplicates
			for (String id : conceptIds) {
				id = id.trim();
				if (!id.equals("") && !requestConceptIds.contains(id)) //remove whitespace, blank lines, and duplicates
					requestConceptIds.add(id);
			}

			Collection<Concept> deletedConcepts = new HashSet<Concept>();

			// loop over original concept answers to find any deleted answers
			for (Concept concept : originalConcepts) {
				boolean conceptDeleted = true;
				for (String conceptId : requestConceptIds) {
					Integer id = getConceptId(conceptId);
					if (id.equals(concept.getConceptId())) {
						conceptDeleted = false;
					}
				}
				if (conceptDeleted)
					deletedConcepts.add(concept);
			}

			// loop over those deleted answers to delete them
			for (Concept concept : deletedConcepts) {
				originalConcepts.remove(concept);
			}

			// loop over concept ids in the request to add any that are new
			for (String conceptId : requestConceptIds) {
				Integer id = getConceptId(conceptId);
				boolean newConcept = true;
				for (Concept originalConcept : originalConcepts) {
					if (id.equals(originalConcept.getConceptId())) {
						newConcept = false;
					}
				}
				// if the current request answer is new, add it to the originals
				if (newConcept) {
					Concept concept = cs.getConcept(id);
					originalConcepts.add(concept);
				}
			}

			log.debug("originalConcepts.getConceptId(): ");
			for (Concept a : originalConcepts)
				log.debug("id: " + a.getConceptId());

			log.debug("requestConceptIds: ");
			for (String i : requestConceptIds)
				log.debug("id: " + i);
		} else {
			originalConcepts.clear();
		}

		setValue(originalConcepts);
	}

	/**
	 * Parses the string and returns the Integer concept id Expected string: "123" or "123^34"
	 * ("conceptId^drugId")
	 *
	 * @param conceptId
	 * @return
	 */
	private Integer getConceptId(String conceptId) {
		if (conceptId.contains("^"))
			return Integer.valueOf(conceptId.substring(0, conceptId.indexOf("^")));
		else
			return Integer.valueOf(conceptId);
	}

}
