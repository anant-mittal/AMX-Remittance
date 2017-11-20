package com.amx.jax.controller;

import static com.amx.jax.constant.ApiEndpoint.ONLINE_CUSTOMER_ENDPOINT;

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
import com.amx.jax.repository.OracleCustomerRespository;
import com.amx.jax.services.OracleCustomerService;
import com.amx.jax.userservice.model.UserModel;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(ONLINE_CUSTOMER_ENDPOINT)
public class OnlineCustomerController {

	@Autowired
	private MetaData metaData;

	@Autowired
	private ConverterUtil converterUtil;
	
	@Autowired
 OracleCustomerService service;

	@RequestMapping(value = "/nationality-id/{nationality-id}/validate", method = RequestMethod.GET)
	public ApiResponse getUser(@PathVariable("nationality-id") String nationalityId) {

		ApiResponse response = null;//metaData.getServiceFactory().getCustomerService()
				//.validateNationalityId(nationalityId);
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
}
