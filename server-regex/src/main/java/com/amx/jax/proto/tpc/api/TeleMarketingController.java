package com.amx.jax.proto.tpc.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.cache.box.CustomerOnCall.CustomerCall;
import com.amx.jax.client.branch.BranchUserClient;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.proto.tpc.api.TPCStatus.ApiTPCStatus;
import com.amx.jax.proto.tpc.api.TPCStatus.TPCServerCodes;
import com.amx.jax.scope.VendorContext.ApiVendorHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "TeleMarketing APIs", tags = "List of APIs to communicate with Telemarketing Service")
@RequestMapping(produces = { CommonMediaType.APPLICATION_JSON_VALUE })
public class TeleMarketingController {

	@Autowired
	BranchUserClient branchUserClient;

	@ApiVendorHeaders
	@ApiOperation(value = "Create Customer Call Session")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CLIENT_CREDS })
	@RequestMapping(value = { "tpc/tm/customer/call/session" }, method = { RequestMethod.POST })
	public AmxApiResponse<CustomerCall, Object> customerCallSession(@RequestParam BigDecimal agentId,
			@RequestParam BigDecimal customerId,
			@RequestParam(required = false) String mobile,
			@RequestParam(required = false) String leadId) {
		return branchUserClient.customerCallSession(agentId, customerId, mobile, leadId);
	}

	@ApiVendorHeaders
	@ApiOperation(value = "Update Customer Call Session")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CLIENT_CREDS })
	@RequestMapping(value = { "tpc/tm/customer/call/status" }, method = { RequestMethod.POST })
	public AmxApiResponse<CustomerCall, Object> customerCallStatus(
			@RequestParam BigDecimal agentId,
			@RequestParam(required = false) String mobile, @RequestParam(required = false) String sessionId,
			@RequestParam(required = false) String leadId,
			@RequestParam String status, @RequestParam String comment,
			@RequestParam BigDecimal customerId) {
		return branchUserClient.customerCallStatus(agentId, mobile, sessionId, leadId, status, comment, customerId);

	}
}
