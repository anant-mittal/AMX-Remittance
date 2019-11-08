package com.amx.jax.branch.controller;

import com.amx.jax.IDiscManagementService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.beans.BranchSession;
import com.amx.jax.client.ExchRateMgmtClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Discount Management APIs")
@ApiStatusService(IDiscManagementService.class)
public class ExchRateMgmtController {

	@Autowired
	ExchRateMgmtClient exchRateMgmtClient;

	@Autowired
	MetaClient metaClient;

	@Autowired
	BranchSession branchSession;

	@Autowired
	private SSOUser ssoUser;
	private static final Logger LOGGER = LoggerService.getLogger(BDiscountMgmtController.class);

	@RequestMapping(value = "/api/exch/rate/inquiry", method = { RequestMethod.POST })
	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(
			@RequestBody ExchRateEnquiryReqDto rateEnquiryReqDto) {
		return exchRateMgmtClient.enquireExchangeRates(rateEnquiryReqDto);
	}
	
	
}
