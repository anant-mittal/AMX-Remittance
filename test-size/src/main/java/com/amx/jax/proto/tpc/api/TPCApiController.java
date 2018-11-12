package com.amx.jax.proto.tpc.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiDataResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiMetaResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiResultsResponse;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.proto.tpc.api.TPCApiConstants.TPCApiClientHeaders;
import com.amx.jax.proto.tpc.api.TPCApiConstants.TPCApiCustomerHeaders;
import com.amx.jax.proto.tpc.api.TPCStatus.ApiTPCStatus;
import com.amx.jax.proto.tpc.api.TPCStatus.TPCServerCodes;
import com.amx.jax.proto.tpc.models.ClientAuth.ClientAuthRequest;
import com.amx.jax.proto.tpc.models.ClientAuth.ClientAuthResponse;
import com.amx.jax.proto.tpc.models.CustomerAuth.CustomerAuthRequest;
import com.amx.jax.proto.tpc.models.CustomerAuth.CustomerAuthResponse;
import com.amx.jax.proto.tpc.models.CustomerDetails;
import com.amx.jax.proto.tpc.models.CustomerBeneDTO;
import com.amx.jax.proto.tpc.models.PurposeOfTrnxDTO;
import com.amx.jax.proto.tpc.models.SourceOfFundDTO;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryRequest;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitVerifyRequest;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitConfirmResponse;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryResponse;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitVerifyResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Third Party Client APIs")
@RequestMapping(produces = {
		CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
public class TPCApiController {

	@ApiOperation(value = "Client Authentication")
	@RequestMapping(value = { TPCApiConstants.Path.CLIENT_AUTH }, method = { RequestMethod.POST })
	public ApiMetaResponse<ClientAuthResponse> auth(@RequestBody ClientAuthRequest authRequest) {
		return AmxApiResponse.buildMeta(new ClientAuthResponse());
	}

	@ApiOperation(
		value = "Client is required to call this api to inititate customer login and redirect customer to url received in response")
	@TPCApiClientHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_LOGIN }, method = { RequestMethod.GET })
	public ApiMetaResponse<CustomerAuthResponse> customerLogin(@RequestBody CustomerAuthRequest auth) {
		return AmxApiResponse.buildMeta(new CustomerAuthResponse());
	}

	@ApiOperation(value = "To fetch customer details and defaults")
	@TPCApiCustomerHeaders
	@ApiTPCStatus({ TPCServerCodes.INVALID_CUSTOMER_TOKEN, TPCServerCodes.CUSTOMER_AUTH_SUCCESS })
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_DETAILS }, method = { RequestMethod.GET })
	public ApiMetaResponse<CustomerDetails> customerDetails() {
		return AmxApiResponse.buildMeta(new CustomerDetails());
	}

	@ApiOperation(value = "To fetch list of beneficaries for customer")
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_BENE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<CustomerBeneDTO> fetchBeneficiaryList() {
		return AmxApiResponse.build(new CustomerBeneDTO());
	}

	@ApiOperation(value = "To fetch list of applicable sources of income")
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_SOURCE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<SourceOfFundDTO> fetchFundSourceList() {
		return AmxApiResponse.build(new SourceOfFundDTO());
	}

	@ApiOperation(value = "To fetch list of applicable purpose of transaction")
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_PURPOSE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<PurposeOfTrnxDTO> fetchPurposeList() {
		return AmxApiResponse.build(new PurposeOfTrnxDTO());
	}

	@ApiOperation(value = "To fetch the exchange rate for inputs provided")
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_INQUIRY }, method = { RequestMethod.POST })
	public ApiDataResponse<RemitInquiryResponse> inquireRemitTranx(
			@RequestBody RemitInquiryRequest remitInquiryRequest
	) {
		return AmxApiResponse.buildData(new RemitInquiryResponse());
	}

	@ApiOperation(value = "To intiate remit transaction. OTP will be sent to customer on his registered mobile.")
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_CONFIRM }, method = { RequestMethod.POST })
	public ApiDataResponse<RemitConfirmResponse> confirmRemitTranx(
			@RequestBody RemitInquiryRequest remitInquiryRequest
	) {
		return AmxApiResponse.buildData(new RemitConfirmResponse());
	}

	@ApiOperation(value = "To verify and complete remit transaction")
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_VERIFY }, method = { RequestMethod.POST })
	public ApiDataResponse<RemitVerifyResponse> verifyRemitTranx(@RequestBody RemitVerifyRequest remitVerifyRequest) {
		return AmxApiResponse.buildData(new RemitVerifyResponse());
	}

}
