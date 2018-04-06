package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.JAX_FIELD_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.request.AddJaxFieldRequest;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.JaxFieldService;

@RestController
@RequestMapping(JAX_FIELD_ENDPOINT)
@SuppressWarnings("rawtypes")
public class JaxFieldController {

	private Logger logger = Logger.getLogger(JaxFieldController.class);

	@Autowired
	MetaData metaData;

	@Autowired
	JaxFieldService jaxFieldService;

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public ApiResponse getJaxFieldsForEntity(@RequestBody GetJaxFieldRequest request) {
		logger.info("In getJaxFieldsForEntity  with entity: " + request.toString());
		ApiResponse response = jaxFieldService.getJaxFieldsForEntity(request);
		return response;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ApiResponse addJaxFieldsForEntity(@RequestBody AddJaxFieldRequest request) {
		logger.info("In addJaxFieldsForEntity  with entity: " + request.toString());
		ApiResponse response = jaxFieldService.addJaxField(request);
		return response;
	}

}
