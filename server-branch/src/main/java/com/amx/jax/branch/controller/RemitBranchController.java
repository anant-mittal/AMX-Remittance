package com.amx.jax.branch.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;

import io.swagger.annotations.Api;

@PreAuthorize("hasPermission('CUSTOMER_MGMT.FXORDER', 'VIEW')")
@RestController
@Api(value = "Remit  APIs")
public class RemitBranchController {

	@RequestMapping(value = "/api/remit/order/list", method = { RequestMethod.GET })
	public AmxApiResponse<FcSaleOrderManagementDTO, Object> getOrderList() {
		return null;
	}

}
