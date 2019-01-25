package com.amx.jax.branch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Remit  APIs")
public class CustomerBranchController {

	@Autowired
	OffsiteCustRegClient offsiteCustRegClient;

//	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.GET })
//	public AmxApiResponse<FcSaleOrderManagementDTO, Object> getCustomerDetails(@RequestParam String identity) {
//		//return offsiteCustRegClient.getOffsiteCustomerData(identity, null);
//	}

}
