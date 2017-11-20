package com.amx.jax.controller;

import static com.amx.jax.constant.ApiEndpoint.CUSTOMER_ENDPOINT;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.dbmodel.OracleCustomer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.services.OracleCustomerService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(CUSTOMER_ENDPOINT)
public class CustomerController {

	@Autowired
	private MetaData metaData;

	@Autowired
	private ConverterUtil converterUtil;

	@Autowired
	private OracleCustomerService service;
	
	@Autowired
	private UserService userSerivce;

	@RequestMapping(value = "/nationality-id/{nationality-id}/validate", method = RequestMethod.GET)
	public ApiResponse getUser(@PathVariable("nationality-id") String nationalityId) {

		ApiResponse response = null;// metaData.getServiceFactory().getCustomerService()
		// .validateNationalityId(nationalityId);
		return response;
	}

	@RequestMapping(value = "/customer/{cust-id}", method = RequestMethod.GET)
	public ApiResponse getCust(@PathVariable("cust-id") BigDecimal custId) {

		ApiResponse response = service.getOracleCust(custId);
		return response;
	}

	@RequestMapping(value = "/savecustomer", method = RequestMethod.POST)
	public ApiResponse saveCust(@RequestBody String json) {
		OracleCustomer userModel = (OracleCustomer) converterUtil.unmarshall(json, service.getModelClass());
		ApiResponse response = service.saveCust(userModel);
		return response;
	}
	
	@RequestMapping(value = "/verify/{civil-id}", method = RequestMethod.GET)
	public ApiResponse verifyCivilId(@PathVariable("civil-id") String civilId) {
		ApiResponse response = userSerivce.verifyCivilId(civilId);
		return response;
	}
}
