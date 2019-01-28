package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.beans.BranchSession;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Customer APIs")
public class CustomerBranchController {

	@Autowired
	OffsiteCustRegClient offsiteCustRegClient;

	@Autowired
	BranchSession branchSession;

	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.GET })
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getCustomerDetails(@RequestParam String identity,
			@RequestParam BigDecimal identityType,
			@RequestParam boolean session) {
		AmxApiResponse<OffsiteCustomerDataDTO, Object> customerResponse = offsiteCustRegClient.getCustomerDetails(
				identity,
				identityType);
		if (session) {
			branchSession.setCustomerId(customerResponse.getResult().getCustomerPersonalDetail().getCustomerId());
		}
		return customerResponse;
	}

}
