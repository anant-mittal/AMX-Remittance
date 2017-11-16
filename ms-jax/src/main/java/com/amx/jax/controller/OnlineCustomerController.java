package com.amx.jax.controller;

import static com.amx.jax.constant.ApiEndpoint.ONLINE_CUSTOMER_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(ONLINE_CUSTOMER_ENDPOINT)
public class OnlineCustomerController {

	@Autowired
	private MetaData metaData;

	@Autowired
	private ConverterUtil converterUtil;

	@RequestMapping(value = "/nationality-id/{nationality-id}/validate", method = RequestMethod.GET)
	public ApiResponse getUser(@PathVariable("nationality-id") String nationalityId) {

		ApiResponse response = null;//metaData.getServiceFactory().getCustomerService()
				//.validateNationalityId(nationalityId);
		return response;
	}
}
