package com.amx.jax.proto.tpc.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiDataMetaResponse;
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
import com.amx.jax.proto.tpc.models.CustomerBeneDTO;
import com.amx.jax.proto.tpc.models.CustomerDetails;
import com.amx.jax.proto.tpc.models.PurposeOfTrnxDTO;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitConfirmPaymentRequest;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitConfirmPaymentResponse;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryRequest;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryResponse;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitVerifyResponse;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemittenceDTO;
import com.amx.jax.proto.tpc.models.SourceOfFundDTO;
import com.amx.utils.CollectionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Third Party Client APIs", tags = "List of APIs to communicate with AMX Service")
@RequestMapping(produces = { CommonMediaType.APPLICATION_JSON_VALUE })
public class TPCApiController {

	@ApiOperation(value = "1: Client Authentication")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CLIENT_CREDS })
	@RequestMapping(value = { TPCApiConstants.Path.CLIENT_AUTH }, method = { RequestMethod.POST })
	public ApiMetaResponse<ClientAuthResponse> auth(@RequestBody ClientAuthRequest authRequest) {
		return AmxApiResponse.buildMeta(new ClientAuthResponse());
	}

	@ApiOperation(value = "2: Customer Authentication",
			notes = "Client is required to call this api to inititate customer login and redirect customer to url received in response")
	@ApiTPCStatus({ TPCServerCodes.INVALID_SESSION_TOKEN })
	@TPCApiClientHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_LOGIN }, method = { RequestMethod.GET })
	public ApiMetaResponse<CustomerAuthResponse> customerLogin(@RequestBody CustomerAuthRequest auth) {
		return AmxApiResponse.buildMeta(new CustomerAuthResponse());
	}

	@ApiOperation(value = "3a: Customer Defaults", notes = "To fetch customer details and defaults")
	@ApiTPCStatus({ TPCServerCodes.INVALID_SESSION_TOKEN, TPCServerCodes.INVALID_CUSTOMER_TOKEN })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_DETAILS }, method = { RequestMethod.GET })
	public ApiMetaResponse<CustomerDetails> customerDetails() {
		return AmxApiResponse.buildMeta(new CustomerDetails());
	}

	@ApiOperation(value = "3b: Beneficiary List", notes = "To fetch list of beneficaries for customer")
	@ApiTPCStatus({ TPCServerCodes.INVALID_SESSION_TOKEN, TPCServerCodes.INVALID_CUSTOMER_TOKEN,
			TPCServerCodes.NO_DATA_FOUND })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_BENE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<CustomerBeneDTO> fetchBeneficiaryList() {
		return AmxApiResponse.build(new CustomerBeneDTO());
	}

	@ApiOperation(value = "3c: Income Sources List", notes = "To fetch list of applicable sources of income")
	@ApiTPCStatus({ TPCServerCodes.INVALID_SESSION_TOKEN, TPCServerCodes.INVALID_CUSTOMER_TOKEN,
			TPCServerCodes.NO_DATA_FOUND })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_SOURCE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<SourceOfFundDTO> fetchFundSourceList() {
		return AmxApiResponse.build(new SourceOfFundDTO());
	}

	@ApiOperation(value = "3d: Purpose Trnx List", notes = "To fetch list of applicable purpose of transaction")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CUSTOMER_TOKEN, TPCServerCodes.INVALID_SESSION_TOKEN,
			TPCServerCodes.NO_DATA_FOUND })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_PURPOSE_LIST }, method = { RequestMethod.GET })
	public ApiResultsResponse<PurposeOfTrnxDTO> fetchPurposeList() {
		return AmxApiResponse.build(new PurposeOfTrnxDTO());
	}

	@ApiOperation(value = "4: Remit Inquiry",
			notes = "To fetch the exchange rate for inputs provided, so that rate could be shown to customer on screen, before he remits")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CUSTOMER_TOKEN, TPCServerCodes.INVALID_SESSION_TOKEN })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_INQUIRY }, method = { RequestMethod.POST })
	public ApiDataMetaResponse<RemitInquiryResponse, List<JaxConditionalFieldDto>> inquireRemitTranx(
			@RequestBody RemitInquiryRequest remitInquiryRequest) {
		return AmxApiResponse.buildData(new RemitInquiryResponse(),
				CollectionUtil.getList(new JaxConditionalFieldDto()));
	}

	@ApiOperation(value = "5: Confirm after Payment",
			notes = "To confirm transaction after payment is done by customer")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CUSTOMER_TOKEN, TPCServerCodes.INVALID_SESSION_TOKEN })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_PAYMENT }, method = { RequestMethod.POST })
	public ApiDataMetaResponse<RemitConfirmPaymentResponse, List<JaxConditionalFieldDto>> confirmPaymentRemitTranx(
			@RequestBody RemitConfirmPaymentRequest remitVerifyRequest) {
		return AmxApiResponse.buildData(new RemittenceDTO(),
				CollectionUtil.getList(new JaxConditionalFieldDto()));
	}

	@ApiOperation(value = "6: Tranx Verification",
			notes = "This API must be implemented by CLIENT to verify remit application details ")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CUSTOMER_TOKEN, TPCServerCodes.INVALID_SESSION_TOKEN })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_VERIFY }, method = { RequestMethod.POST })
	public ApiDataResponse<RemitVerifyResponse> verifyRemitTranx(
			@RequestBody RemitConfirmPaymentRequest remitVerifyRequest) {
		return AmxApiResponse.buildData(new RemittenceDTO());
	}

	@ApiOperation(value = "7: Transaction Status",
			notes = "To check status of transaction after payment is done by customer")
	@ApiTPCStatus({ TPCServerCodes.INVALID_CUSTOMER_TOKEN, TPCServerCodes.INVALID_SESSION_TOKEN })
	@TPCApiCustomerHeaders
	@RequestMapping(value = { TPCApiConstants.Path.CUSTOMER_REMIT_STATUS }, method = { RequestMethod.POST })
	public ApiDataResponse<RemitConfirmPaymentResponse> statusRemitTranx(
			@RequestParam String applicationId) {
		return AmxApiResponse.buildData(new RemittenceDTO(),
				CollectionUtil.getList(new JaxConditionalFieldDto()));
	}

}
