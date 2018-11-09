package com.amx.jax.tpc.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiDataResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiMetaResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiResultsResponse;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.tpc.models.ModelCustomerBene;
import com.amx.jax.tpc.models.RequestClientAuth;
import com.amx.jax.tpc.models.RequestRemitInquiry;
import com.amx.jax.tpc.models.RequestRemitVerify;
import com.amx.jax.tpc.models.ResponseClientAuth;
import com.amx.jax.tpc.models.ResponseRemitConfirm;
import com.amx.jax.tpc.models.ResponseRemitInquiry;
import com.amx.jax.tpc.models.ResponseRemitVerify;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Third Party Client APIs")
@ApiStatusService(IDeviceService.class)
public class TPCApiController {

	@ApiOperation(value = "Client Authentication")
	@RequestMapping(value = { TPCApiConstants.Path.CLIENT_AUTH }, method = { RequestMethod.POST })
	public ApiMetaResponse<ResponseClientAuth> auth(@RequestBody RequestClientAuth authRequest) {
		return AmxApiResponse.buildMeta(new ResponseClientAuth());
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_LOGIN }, method = { RequestMethod.GET })
	public ApiMetaResponse<ResponseClientAuth> customerLogin() {
		return AmxApiResponse.buildMeta(new ResponseClientAuth());
	}

	@ApiOperation(
			value = "Client is required to implement this API on whitelisted server for AMX to call, once customer auth is completed",
			notes = "Reponse should be REDIRECT:https://clientsite.com/amx/redirect/after/callback"
	)
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_LOGIN_CALLBACK }, method = { RequestMethod.GET })
	public String customerAuthCallback() {
		return "REDIRECT:https://clientsite.com/amx/redirect/after/callback";
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_DETAILS }, method = { RequestMethod.GET })
	public ApiMetaResponse<ResponseClientAuth> customerDetails() {
		return AmxApiResponse.buildMeta(new ResponseClientAuth());
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_BENE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<ModelCustomerBene> fetchBeneficiaryList() {
		return AmxApiResponse.build(new ModelCustomerBene());
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_SOURCE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<ResponseClientAuth> fetchFundSourceList() {
		return AmxApiResponse.build(new ResponseClientAuth());
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_PURPOSE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<ResponseClientAuth> fetchPurposeList() {
		return AmxApiResponse.build(new ResponseClientAuth());
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_INQUIRY }, method = { RequestMethod.POST })
	public ApiDataResponse<ResponseRemitInquiry> inquireRemitTranx(
			@RequestBody RequestRemitInquiry remitInquiryRequest
	) {
		return AmxApiResponse.buildData(new ResponseRemitInquiry());
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_CONFIRM }, method = { RequestMethod.POST })
	public ApiDataResponse<ResponseRemitConfirm> confirmRemitTranx(
			@RequestBody RequestRemitInquiry remitInquiryRequest
	) {
		return AmxApiResponse.buildData(new ResponseRemitConfirm());
	}

	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_VERIFY }, method = { RequestMethod.POST })
	public ApiDataResponse<ResponseRemitVerify> verifyRemitTranx(@RequestBody RequestRemitVerify remitVerifyRequest) {
		return AmxApiResponse.buildData(new ResponseRemitVerify());
	}

}
