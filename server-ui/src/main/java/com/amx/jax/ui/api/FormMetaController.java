package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.GetJaxFieldRequest;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;

import io.swagger.annotations.Api;

/**
 * The Class FormMetaController.
 */
@RestController
@Api(value = "Form Meta APIs : List of fields")
public class FormMetaController {

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/**
	 * Gets the list of countries.
	 *
	 * @param req
	 *            the req
	 * @return the list of countries
	 */
	@RequestMapping(value = "/api/form/fields/entity", method = { RequestMethod.POST })
	public ResponseWrapper<List<JaxConditionalFieldDto>> getListOfCountries(@RequestBody GetJaxFieldRequest req) {
		return new ResponseWrapper<List<JaxConditionalFieldDto>>(
				jaxService.setDefaults().getJaxFieldClient().getJaxFieldsForEntity(req).getResults());
	}

	/**
	 * Gets the list of states for country.
	 *
	 * @param countryId
	 *            the country id
	 * @return the list of states for country
	 */
	@RequestMapping(value = "/api/form/fields/bnfcry", method = { RequestMethod.GET })
	public ResponseWrapper<List<JaxConditionalFieldDto>> getListOfStatesForCountry(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<JaxConditionalFieldDto>>(
				jaxService.setDefaults().getJaxFieldClient().getDynamicFieldsForBeneficiary(countryId).getResults());
	}

}
