package com.amx.jax.proto.tpc.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiDataMetaResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiDataResponse;
import com.amx.jax.api.AmxResponseSchemes.ApiResultsResponse;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.JaxPaymentService;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.TpcClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.proto.tpc.models.CustomerBeneDTO;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitConfirmRequest;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInitRequest;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryRequest;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryResponse;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemitTranxStatusResponse;
import com.amx.jax.proto.tpc.models.RemittenceModels.RemittenceDTO;
import com.amx.jax.proto.tpc.models.RemittenceModels.TranxStatus;
import com.amx.jax.proto.tpc.models.SourceOfFundDTO;
import com.amx.utils.ArgUtil;
import com.amx.utils.StringUtils;

@Component
public class TPCApiRemitService {

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	TpcClient tpcClient;

	@Autowired
	JaxMetaInfo jaxMetaInfoContext;

	@Autowired
	TPCSession tpcSession;

	@Autowired
	RemitClient remitClient;

	@Autowired
	private BeneClient beneClient;

	public ApiResultsResponse<CustomerBeneDTO> fetchBeneficiaryList() {
		List<BeneficiaryListDTO> x = beneClient.getBeneficiaryList(new BigDecimal(0)).getResults();

		AmxApiResponse<CustomerBeneDTO, Object> resp = new AmxApiResponse<CustomerBeneDTO, Object>();
		for (BeneficiaryListDTO beneficiaryListDTO : x) {
			CustomerBeneDTO y = new CustomerBeneDTO();
			y.id = beneficiaryListDTO.getBeneficiaryRelationShipSeqId();
			y.account = StringUtils.mask(beneficiaryListDTO.getBankAccountNumber());
			y.fullName = beneficiaryListDTO.getBenificaryName();
			y.currency = beneficiaryListDTO.getCurrencyQuoteName();
			y.bank = beneficiaryListDTO.getBankName();
			resp.getResults().add(y);
		}

		return resp;
	}

	public ApiResultsResponse<SourceOfFundDTO> fetchSourceList() {
		List<SourceOfIncomeDto> x = remitClient.getSourceOfIncome().getResults();

		AmxApiResponse<SourceOfFundDTO, Object> resp = new AmxApiResponse<SourceOfFundDTO, Object>();

		for (SourceOfIncomeDto sourceOfIncomeDto : x) {
			SourceOfFundDTO y = new SourceOfFundDTO();
			y.code = sourceOfIncomeDto.getShortDesc();
			y.description = sourceOfIncomeDto.getDescription();
			y.id = sourceOfIncomeDto.getSourceofIncomeId();
			resp.getResults().add(y);
		}

		return resp;
	}

	public AmxApiResponse<RemitInquiryResponse, List<JaxConditionalFieldDto>> inquire(
			RemitInquiryRequest remitInquiryRequest) {
		RemittanceTransactionRequestModel model = new RemittanceTransactionRequestModel();
		model.setBeneId(remitInquiryRequest.beneId);
		model.setLocalAmount(remitInquiryRequest.domAmount);
		model.setSourceOfFund(remitInquiryRequest.source);
		model.setAvailLoyalityPoints(false);

		AmxApiResponse<RemittanceTransactionResponsetModel, List<JaxConditionalFieldDto>> x = remitClient
				.validateTransactionV2(model);
		RemittanceTransactionResponsetModel respX = x.getResult();

		RemitInquiryResponse resp = new RemitInquiryResponse();
		resp.beneId = remitInquiryRequest.beneId;
		resp.forAmount = respX.getExRateBreakup().getConvertedFCAmount();
		resp.domAmount = respX.getExRateBreakup().getConvertedLCAmount();
		resp.rate = respX.getExRateBreakup().getRate();

		return AmxApiResponse.buildData(resp, x.getMeta());
	}

	public ApiDataMetaResponse<RemitTranxStatusResponse, List<JaxConditionalFieldDto>> initiate(
			RemitInitRequest remitConfirmRequest) {

		RemittanceTransactionRequestModel model = new RemittanceTransactionRequestModel();
		model.setBeneId(remitConfirmRequest.beneId);
		model.setLocalAmount(remitConfirmRequest.domAmount);
		model.setSourceOfFund(remitConfirmRequest.source);
		model.setAvailLoyalityPoints(false);
		model.setAdditionalFields(remitConfirmRequest.additionalFields);
		model.setFlexFields(remitConfirmRequest.flexFields);

		// model.setSrlId(new BigDecimal(22));
		ExchangeRateBreakup exRateBreakup = new ExchangeRateBreakup();
		exRateBreakup.setRate(remitConfirmRequest.rate);
		model.setExRateBreakup(exRateBreakup);

		ApiResponse<RemittanceApplicationResponseModel> x = remitClient
				.saveTransaction(model);
		RemittanceApplicationResponseModel respX = x.getResult();

		RemitTranxStatusResponse resp = new RemittenceDTO();
		resp.setApplicationId(ArgUtil.parseAsString(respX.getRemittanceAppId()));
		resp.setTransactionId(null);
		resp.setTranxStatus(TranxStatus.INITIATED);

		return AmxApiResponse.buildData(resp, null);
	}

	public ApiDataResponse<RemitTranxStatusResponse> confirm(
			RemitConfirmRequest remitVerifyRequest) {

		remitVerifyRequest.getApplicationId();

		PaymentResponseDto request = new PaymentResponseDto();
		request.setAuth_appNo("471599");
		request.setPaymentId(remitVerifyRequest.getPaymntReference());
		// app docno
		request.setUdf3("27002498");
		request.setResultCode(remitVerifyRequest.getPaymentStatus().toString());
		// cusref
		request.setTrackId("90277");
		request.setReferenceId("801813658796");
		request.setTransactionId("9272568121380181");
		request.setPostDate("0199");

		return null;
	}

	@Autowired
	JaxPaymentService jaxPaymentService;

	public ApiDataResponse<RemitTranxStatusResponse> getApplicationStatusByAppId(String applicationId) {

		RemittanceTransactionStatusResponseModel x = remitClient
				.getApplicationStatusByAppId(ArgUtil.parseAsBigDecimal(applicationId)).getResult();

		RemitTranxStatusResponse resp = new RemittenceDTO();
		resp.setApplicationId(applicationId);
		resp.setTransactionId(x.getTransactionHistroyDTO().getTrnxIdNumber());
		resp.setTranxStatus(TranxStatus.valueOf(x.getStatus().name()));

		return AmxApiResponse.buildData(resp);
	}

}
