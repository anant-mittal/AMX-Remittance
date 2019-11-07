package com.amx.jax.offsite.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.cache.box.CustomerOnCall.CustomerCall;
import com.amx.jax.client.branch.BranchUserClient;
import com.amx.jax.client.branch.IBranchService;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.scope.VendorContext.ApiVendorHeaders;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "TeleMarketing APIs", tags = "For Centrix")
@RequestMapping(produces = { CommonMediaType.APPLICATION_JSON_VALUE })
@ApiStatusService(IBranchService.class)
public class TeleMarketingController {

	private static final String TELEMARKETING = "TELEMARKETING";

	@Autowired
	BranchUserClient branchUserClient;

	@ApiVendorHeaders
	@ApiOperation(value = "Create Customer Call Session")
	@ApiRequest(feature = TELEMARKETING)
	@RequestMapping(value = { "tpc/tm/customer/call/session" }, method = { RequestMethod.POST })
	public AmxApiResponse<CustomerCall, Object> customerCallSession(@RequestParam BigDecimal agentId,
			@RequestParam(required = false) BigDecimal customerId,
			@RequestParam(required = false) BigDecimal leadId) {
		return branchUserClient.customerCallSession(agentId, customerId, leadId);
	}

	@ApiVendorHeaders
	@ApiOperation(value = "Update Customer Call Session")
	@ApiRequest(feature = TELEMARKETING)
	@RequestMapping(value = { "tpc/tm/customer/call/status" }, method = { RequestMethod.POST })
	public AmxApiResponse<CustomerCall, Object> customerCallStatus(
			@RequestParam BigDecimal agentId, @RequestParam(required = false) String sessionId,
			@RequestParam(required = false) BigDecimal leadId,
			@RequestParam String followUpCode, @RequestParam String remark,
			@RequestParam(required = false) BigDecimal customerId) {
		return branchUserClient.customerCallStatus(agentId, customerId, leadId, followUpCode, remark, sessionId);

	}
}
