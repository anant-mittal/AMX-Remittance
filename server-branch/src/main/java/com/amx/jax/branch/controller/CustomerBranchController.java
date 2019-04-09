package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.beans.BranchSession;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.client.branch.BranchUserClient;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Customer APIs")
public class CustomerBranchController {

	@Autowired
	OffsiteCustRegClient offsiteCustRegClient;

	@Autowired
	BranchSession branchSession;

	@Autowired
	private BranchUserClient branchUserClient;

	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.POST })
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> setCustomerDetails(@RequestParam String identity,
			@RequestParam BigDecimal identityType,
			@RequestParam boolean session) {
		AmxApiResponse<OffsiteCustomerDataDTO, Object> customerResponse = offsiteCustRegClient
				.getOffsiteCustomerDetails(
						identity,
						identityType);
		if (session) {
			branchSession.setCustomer(customerResponse.getResult());
		}
		return customerResponse;
	}

	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.GET })
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getCustomerDetails() {
		return AmxApiResponse.build(branchSession.getCustomer());
	}

	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.DELETE })
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> clearCustomerDetails() {
		branchSession.setCustomer(null);
		return AmxApiResponse.build(branchSession.getCustomer());
	}

	@RequestMapping(value = "/api/customer/trnxdata", method = { RequestMethod.GET })
	public AmxApiResponse<UserwiseTransactionDto, Object> fetchCustomerTrnxInfo(
			@RequestParam(required = false) String transactiondate) {
		return branchUserClient.getTotalCount(transactiondate);
	}

}
