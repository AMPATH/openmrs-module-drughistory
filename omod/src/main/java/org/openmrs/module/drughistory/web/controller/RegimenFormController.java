package org.openmrs.module.drughistory.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.drughistory.Regimen;
import org.openmrs.module.drughistory.api.RegimenService;
import org.openmrs.module.drughistory.web.propertyeditor.ConceptListEditor;
import org.openmrs.web.WebConstants;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * Controller for Regimen form page
 */
@Controller
@RequestMapping("module/drughistory/regimen.form")
public class RegimenFormController {

	private static final String SUCCESS_VIEW = "redirect:regimen.list";
	private static final String EDIT_VIEW = "module/drughistory/regimenForm";

	private Log log = LogFactory.getLog(this.getClass());

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		Regimen regimen = (Regimen) binder.getTarget();

		binder.registerCustomEditor(java.util.Collection.class, "drugs",
				new ConceptListEditor(regimen == null ? null : regimen.getDrugs()));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String editRegimen(
			@RequestParam(value = "id", required = false) Integer regimenId,
			ModelMap modelMap) {

		Regimen regimen = null;

		if (regimenId != null)
			regimen = Context.getService(RegimenService.class).getRegimen(regimenId);

		if (regimen == null)
			regimen = new Regimen();

		modelMap.put("regimen", regimen);
		modelMap.put("allLines", Arrays.asList(Regimen.LINE_FIRST, Regimen.LINE_SECOND));
		modelMap.put("allAges", Arrays.asList(Regimen.AGE_ADULT, Regimen.AGE_PEDS));

		return EDIT_VIEW;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String saveRegimen(
			@ModelAttribute("regimen") Regimen regimen,
			BindingResult errors,
			HttpServletRequest request) {

		RegimenService service = Context.getService(RegimenService.class);
		HttpSession httpSession = request.getSession();
		String view = null;

		if (request.getParameter("save") != null) {
			service.saveRegimen(regimen);
			view = SUCCESS_VIEW;
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimen saved");
		}

		// if the user is retiring out the EncounterType
		else if (request.getParameter("retire") != null) {
			String retireReason = request.getParameter("retireReason");
			if (regimen.getId() != null && !(StringUtils.hasText(retireReason))) {
				errors.reject("retireReason", "general.retiredReason.empty");
				return EDIT_VIEW;
			}

			// get the object by id, since that is the only value coming from the form
			regimen = Context.getService(RegimenService.class).getRegimen(regimen.getRegimenId());

			service.retireRegimen(regimen, retireReason);
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimen successfully retired");

			view = SUCCESS_VIEW;
		}

		// if the user is un-retiring out the EncounterType
		else if (request.getParameter("unretire") != null) {

			// get the object by id, since that is the only value coming from the form
			regimen = Context.getService(RegimenService.class).getRegimen(regimen.getRegimenId());

			service.unretireRegimen(regimen);
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimen successfully unretired");

			view = SUCCESS_VIEW;
		}

		// if the user is purging the encounterType
		else if (request.getParameter("purge") != null) {

			try {
				// get the object by id, since that is the only value coming from the form
				regimen = Context.getService(RegimenService.class).getRegimen(regimen.getRegimenId());

				service.purgeRegimen(regimen);
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimen successfully purged");
				view = SUCCESS_VIEW;
			} catch (DataIntegrityViolationException e) {
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "error.object.inuse.cannot.purge");
				view = EDIT_VIEW;
			} catch (APIException e) {
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "error.general: " + e.getLocalizedMessage());
				view = EDIT_VIEW;
			}
		}

		return view;
	}
}
