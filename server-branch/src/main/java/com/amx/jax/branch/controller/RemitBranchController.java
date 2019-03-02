package com.amx.jax.branch.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.api.ListRequestModel;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.rbaac.IRbaacService;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.terminal.TerminalService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Remit  APIs")
@ApiStatusService(IRbaacService.class)
public class RemitBranchController {

	@Autowired
	private RemitClient remitClient;

	@Autowired
	private RemittanceClient branchRemittanceClient;

	@Autowired
	private BeneClient beneClient;

	@Autowired
	TerminalService terminalService;

	@Autowired
	private SSOUser ssoUser;

	@Autowired
	private PostManService postManService;

	@Autowired
	private HttpServletResponse response;

	@RequestMapping(value = "/api/remitt/order/list", method = { RequestMethod.GET })
	public AmxApiResponse<FcSaleOrderManagementDTO, Object> getOrderList() {
		return null;
	}

	@RequestMapping(value = "/api/remitt/purpose/list", method = { RequestMethod.POST })
	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeList(@RequestParam BigDecimal beneId) {
		return branchRemittanceClient.getPurposeOfTrnx(beneId);
	}

	@RequestMapping(value = "/api/remitt/income_sources/list", method = { RequestMethod.POST })
	public AmxApiResponse<SourceOfIncomeDto, Object> getSourceOfIncome() {
		return AmxApiResponse.buildList(remitClient.getSourceOfIncome().getResults());
	}

	@RequestMapping(value = "/api/remitt/routing/list", method = { RequestMethod.POST })
	public AmxApiResponse<RoutingResponseDto, Object> getRoutingSetupDeatils(@RequestParam BigDecimal beneId,
			@RequestParam(required = false) BigDecimal serviceId) {

		if (ArgUtil.isEmpty(serviceId)) {
			return branchRemittanceClient.getRoutingSetupDeatils(beneId);
		}
		return branchRemittanceClient.getRoutingDetailsByServiceId(beneId, serviceId);
	}

	@RequestMapping(value = "/api/remitt/bnfcry/list", method = { RequestMethod.POST })
	public AmxApiResponse<BeneficiaryListDTO, Object> beneList() {
		return AmxApiResponse.buildList(beneClient.getBeneficiaryList(new BigDecimal(0)).getResults());
	}

	@RequestMapping(value = "/api/remitt/default", method = { RequestMethod.POST })
	public AmxApiResponse<RemittancePageDto, Object> defaultBeneficiary(
			@RequestParam(required = false) BigDecimal beneId,
			@RequestParam(required = false) BigDecimal transactionId) {
		return AmxApiResponse.buildList(beneClient.defaultBeneficiary(beneId, transactionId).getResults());
	}

	@RequestMapping(value = "/api/remitt/tranxrate", method = { RequestMethod.POST })
	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> bnfcryCheck(
			@RequestBody BranchRemittanceGetExchangeRateRequest request) {
		return AmxApiResponse.buildList(branchRemittanceClient.getExchaneRate(request).getResults());
	}

	@RequestMapping(value = "/api/remitt/save", method = { RequestMethod.POST })
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(
			@RequestBody BranchRemittanceApplRequestModel requestModel) {
		return branchRemittanceClient.saveBranchRemittanceApplication(requestModel);
	}

	@RequestMapping(value = "/api/remitt/cart/submit", method = { RequestMethod.POST })
	public AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(
			@RequestBody BranchRemittanceRequestModel requestModel) {
		return branchRemittanceClient.saveRemittanceTransaction(requestModel);
	}

	@RequestMapping(value = "/api/remitt/cart/fetch", method = { RequestMethod.POST })
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> fetchCustomerShoppingCart(
			@RequestBody BranchRemittanceApplRequestModel requestModel) {
		return branchRemittanceClient.fetchCustomerShoppingCart();
	}

	/// Fetch

	@RequestMapping(value = "/api/remitt/payment_mode/list", method = { RequestMethod.GET })
	public AmxApiResponse<PaymentModeOfPaymentDto, Object> fetchModeOfPayment() {
		return branchRemittanceClient.fetchModeOfPayment();
	}

	@RequestMapping(value = "/api/remitt/local_bank/list", method = { RequestMethod.GET })
	public AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks() {
		return branchRemittanceClient.fetchLocalBanks();
	}

	@RequestMapping(value = "/api/remitt/customer_local_bank/list", method = { RequestMethod.GET })
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks() {
		return branchRemittanceClient.fetchCustomerLocalBanks();
	}

	@RequestMapping(value = "/api/remitt/customer_bank/name", method = { RequestMethod.GET })
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerBankNames(@RequestParam BigDecimal bankId) {
		return branchRemittanceClient.fetchCustomerBankNames(bankId);
	}

	@RequestMapping(value = "/api/remitt/pos_bank/list", method = { RequestMethod.GET })
	public AmxApiResponse<ResourceDTO, Object> fetchPosBanks() {
		return branchRemittanceClient.fetchPosBanks();
	}

	@RequestMapping(value = "/api/remitt/stock/fund", method = { RequestMethod.GET })
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination() {
		return branchRemittanceClient.fetchLocalCurrencyDenomination();
	}

	@RequestMapping(value = "/api/remitt/stock/refund", method = { RequestMethod.GET })
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination() {
		return branchRemittanceClient.fetchLocalCurrencyRefundDenomination();
	}

	@RequestMapping(value = "/api/remitt/customer_bank/save", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(
			@RequestBody ListRequestModel<CustomerBankRequest> customerbanks) {
		return branchRemittanceClient.saveCustomerBankDetails(customerbanks.getValues());
	}

	@RequestMapping(value = "/api/remitt/customer_bank/relations", method = { RequestMethod.GET })
	public AmxApiResponse<BeneRelationsDescriptionDto, Object> getBeneficiaryRelations() {
		return AmxApiResponse.buildList(beneClient.getBeneficiaryRelations().getResults());
	}

	@RequestMapping(value = "/api/remitt/status/update", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceStatus(
			@RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		return terminalService.updateRemittanceState(ssoUser.getUserClient().getTerminalId().intValue(),
				ssoUser.getUserDetails().getEmployeeId(), signaturePadRemittanceInfo);
	}

	@ApiOperation(value = "Returns transaction reciept:")
	@RequestMapping(value = "/api/remitt/tranx/report.{ext}", method = { RequestMethod.GET })
	public @ResponseBody String report(@RequestParam(required = false) BigDecimal collectionDocumentNo,
			@RequestParam(required = false) BigDecimal documentNumber,
			@RequestParam(required = false) BigDecimal documentFinanceYear,
			@RequestParam(required = false) BigDecimal collectionDocumentFinYear,
			@RequestParam(required = false) BigDecimal collectionDocumentCode,
			@RequestParam(required = false) BigDecimal customerReference, @PathVariable("ext") String ext,
			@RequestParam(required = false) Boolean duplicate) throws PostManException, IOException {

		duplicate = ArgUtil.parseAsBoolean(duplicate, false);
		// duplicate = (duplicate == null || duplicate.booleanValue() == false) ? false
		// : true;

		TransactionHistroyDTO tranxDTO = new TransactionHistroyDTO();
		tranxDTO.setCollectionDocumentNo(collectionDocumentNo);
		tranxDTO.setDocumentNumber(documentNumber);
		tranxDTO.setDocumentFinanceYear(documentFinanceYear);
		tranxDTO.setCollectionDocumentFinYear(collectionDocumentFinYear);
		tranxDTO.setCollectionDocumentCode(collectionDocumentCode);
		tranxDTO.setCustomerReference(customerReference);

		RemittanceReceiptSubreport rspt = remitClient
				.report(tranxDTO, !duplicate.booleanValue()).getResult();
		AmxApiResponse<RemittanceReceiptSubreport, Object> wrapper = AmxApiResponse.buildData(rspt);
		if ("pdf".equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.REMIT_RECEIPT_COPY_JASPER : TemplatesMX.REMIT_RECEIPT_JASPER,
							wrapper, File.Type.PDF))
					.getResult();
			file.create(response, false);
			return null;
		} else if ("html".equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.REMIT_RECEIPT_COPY_JASPER : TemplatesMX.REMIT_RECEIPT_JASPER,
							wrapper, null))
					.getResult();
			return file.getContent();
		} else {
			return JsonUtil.toJson(wrapper);
		}
	}
}
