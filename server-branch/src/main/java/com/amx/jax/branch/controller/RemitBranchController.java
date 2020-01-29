package com.amx.jax.branch.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branch.BranchOutFilter;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.PayAtBranchClient;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.request.remittance.BenePackageRequest;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.request.remittance.RoutingPricingRequest;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.customer.BenePackageResponse;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CardTypeDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.ParameterDetailsResponseDto;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.model.response.remittance.PaymentModeDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.rbaac.IRbaacService;
import com.amx.jax.response.payatbranch.PayAtBranchTrnxListDTO;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.terminal.TerminalService;
import com.amx.jax.utils.PostManUtil;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	private PayAtBranchClient payAtBranchClient;

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
	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeList(@RequestParam BigDecimal beneId,
			@RequestParam BigDecimal routingCountryId) {
		return branchRemittanceClient.getPurposeOfTrnx(beneId, routingCountryId);
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
	public AmxApiResponse<BeneficiaryListDTO, Object> beneList(
			@RequestParam(required = false, defaultValue = "false") boolean excludePackages) {
		return AmxApiResponse.buildList(beneClient.getBeneficiaryList(new BigDecimal(0), excludePackages).getResults());
	}

	@RequestMapping(value = "/api/remitt/default", method = { RequestMethod.POST })
	public AmxApiResponse<RemittancePageDto, Object> defaultBeneficiary(
			@RequestParam(required = false) BigDecimal beneId,
			@RequestParam(required = false) BigDecimal transactionId) {
		RemittancePageDto remittancePageDto = beneClient.defaultBeneficiary(beneId, transactionId).getResult();
		if (!ArgUtil.isEmpty(beneId)) {
			remittancePageDto
					.setPackages(branchRemittanceClient.getGiftService(beneId).getResult().getParameterDetailsDto());
		}
		return AmxApiResponse.build(remittancePageDto);
	}

	@RequestMapping(value = "/api/remitt/tranxrate", method = { RequestMethod.POST })
	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> bnfcryCheck(
			@RequestBody BranchRemittanceGetExchangeRateRequest request) {
		return branchRemittanceClient.getExchaneRate(request);
	}

	@RequestMapping(value = "/api/remitt/save", method = { RequestMethod.POST })
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(
			@RequestBody BranchRemittanceApplRequestModel requestModel) {
		return branchRemittanceClient.saveBranchRemittanceApplication(requestModel);
	}

	// reference1: https://stackoverflow.com/questions/32132214/why-doesnt-this-stream-lambda-expression-work-with-spel-declaration
	// reference2: https://docs.spring.io/spring/docs/4.3.12.RELEASE/spring-framework-reference/html/expressions.html#expressions-collection-selection
	// if RATE_PROVIDER is doing remittance, he cannot use cash collection mode.
	@PreAuthorize("hasPermission('CUSTOMER_MGMT.PLACE_ORDER.RATE_PROVIDER', 'VIEW') ? #requestModel.getCollctionModeDto().?[#this.getCurrencyDenominationList().size() != 0].size() == 0 : true")
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
	public AmxApiResponse<PaymentModeDto, Object> fetchModeOfPayment() {
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
			@RequestBody CustomerBankRequest customerBankRequest) {
		return branchRemittanceClient.saveCustomerBankDetails(customerBankRequest);
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

	@Autowired
	BranchOutFilter branchMetaOutFilter;

	@ApiOperation(value = "Returns transaction reciept:")
	@RequestMapping(value = "/api/remitt/tranx/report", method = { RequestMethod.GET }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE,
			CommonMediaType.APPLICATION_PDF_VALUE, CommonMediaType.TEXT_HTML_VALUE })
	public ResponseEntity<byte[]> report(@RequestParam(required = false) BigDecimal collectionDocumentNo,
			@RequestParam(required = false) BigDecimal documentNumber,
			@RequestParam(required = false) BigDecimal documentFinanceYear,
			@RequestParam(required = false) BigDecimal collectionDocumentFinYear,
			@RequestParam(required = false) BigDecimal collectionDocumentCode,
			@RequestParam(required = false) BigDecimal customerReference, @RequestParam("ext") File.Type ext,
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
				.report(tranxDTO, !duplicate.booleanValue(), branchMetaOutFilter.exportMeta()).getResult();
		AmxApiResponse<RemittanceReceiptSubreport, Object> wrapper = AmxApiResponse.buildData(rspt);
		if (File.Type.PDF.equals(ext)) {
			File file = postManService.processTemplate(new File(
					duplicate ? TemplatesMX.REMIT_RECEIPT_COPY_JASPER : TemplatesMX.REMIT_RECEIPT_JASPER_NO_HEADER,
					wrapper, File.Type.PDF).lang(AppContextUtil.getTenant().defaultLang())).getResult();
			return PostManUtil.download(file);
			// file.create(response, false);
			// return null;
		} else if (File.Type.HTML.equals(ext)) {
			File file = postManService.processTemplate(new File(
					duplicate ? TemplatesMX.REMIT_RECEIPT_COPY_JASPER : TemplatesMX.REMIT_RECEIPT_JASPER_NO_HEADER,
					wrapper, null)).getResult();
			// return file.getContent();
			return PostManUtil.download(file);
		} else {
			String json = JsonUtil.toJson(AmxApiResponse.build(wrapper));
			return ResponseEntity.ok().contentLength(json.length())
					.contentType(MediaType.valueOf(File.Type.JSON.getContentType())).body(json.getBytes());
		}
		// else {
		// return JsonUtil.toJson(wrapper);
		// }
	}


	@RequestMapping(value = "/api/remitt/cart/report", method = { RequestMethod.GET }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE,
			CommonMediaType.APPLICATION_PDF_VALUE, CommonMediaType.TEXT_HTML_VALUE })
	public ResponseEntity<byte[]> report(@RequestParam("ext") File.Type ext) throws PostManException, IOException {

		// duplicate = (duplicate == null || duplicate.booleanValue() == false) ? false
		AmxApiResponse<BranchRemittanceApplResponseDto, Object> wrapper = branchRemittanceClient
				.fetchCustomerShoppingCart();
		if (File.Type.PDF.equals(ext)) {
			File file = postManService
					.processTemplate(new File(TemplatesMX.REMIT_APPLICATION_RECEIPT_JASPER, wrapper, File.Type.PDF)
							.lang(AppContextUtil.getTenant().defaultLang()))
					.getResult();
			return PostManUtil.download(file);
			// file.create(response, false);
			// return null;
		} else if (File.Type.HTML.equals(ext)) {
			File file = postManService
					.processTemplate(new File(TemplatesMX.REMIT_APPLICATION_RECEIPT_JASPER, wrapper, null)).getResult();
			// return file.getContent();
			return PostManUtil.download(file);
		} else {
			String json = JsonUtil.toJson(AmxApiResponse.build(wrapper));
			return ResponseEntity.ok().contentLength(json.length())
					.contentType(MediaType.valueOf(File.Type.JSON.getContentType())).body(json.getBytes());
		}
		// else {
		// return JsonUtil.toJson(wrapper);
		// }
	}


	@RequestMapping(value = "/api/remitt/tranx/email", method = { RequestMethod.GET })
	public AmxApiResponse<BoolRespModel, Object> sendEmail(
			@RequestParam(required = false) BigDecimal collectionDocumentNo,
			@RequestParam(required = false) BigDecimal collectionDocumentFinYear,
			@RequestParam(required = false) BigDecimal collectionDocumentCode) {
		return branchRemittanceClient.sendReceiptOnEmail(collectionDocumentNo, collectionDocumentFinYear,
				collectionDocumentCode);
	}

	@RequestMapping(value = "/api/remitt/appl/delete", method = { RequestMethod.POST })
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> deleteApplication(
			@RequestParam(required = true) BigDecimal remittanceApplicationId) {
		return branchRemittanceClient.deleteFromShoppingCart(remittanceApplicationId);
	}

	@RequestMapping(value = "/api/remitt/aml/verify", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> validateAmlCredentials(
			@RequestParam(value = "staffUserName", required = true) String staffUserName,
			@RequestParam(value = "staffPassword", required = true) String staffPassword) {
		return branchRemittanceClient.validationStaffCredentials(staffUserName, staffPassword);
	}

	@RequestMapping(value = "/api/remitt/routes", method = { RequestMethod.POST })
	public AmxApiResponse<DynamicRoutingPricingResponse, Object> getDynamicRoutingPricingRoutes(
			@RequestBody RoutingPricingRequest routingPricingRequest) {
		return branchRemittanceClient.getDynamicRoutingPricing(routingPricingRequest);
	}

	@RequestMapping(value = "/api/remitt/pb_trnx/list", method = { RequestMethod.POST })
	public AmxApiResponse<PayAtBranchTrnxListDTO, Object> getPayAtBranchApplList() {
		return payAtBranchClient.getPbTrnxListBranch();
	}

	@RequestMapping(value = "/api/remitt/package/list", method = { RequestMethod.POST })
	public AmxApiResponse<ParameterDetailsResponseDto, Object> getPackages(@RequestParam BigDecimal beneId) {
		return branchRemittanceClient.getGiftService(beneId);
	}

	@RequestMapping(value = "/api/remitt/payment/link", method = { RequestMethod.POST })
	public AmxApiResponse<PaymentLinkRespDTO, Object> sendPaymentLink() {
		return branchRemittanceClient.createAndSendPaymentLink();
	}

	@RequestMapping(value = "/api/remitt/packages/preflex", method = { RequestMethod.POST })
	public AmxApiResponse<BenePackageResponse, Object> getBenePackages(
			@RequestBody BenePackageRequest benePackageRequest) {
		return branchRemittanceClient.getBenePackages(benePackageRequest);
	}

	@RequestMapping(value = "/api/remitt/customer_bank/card_type", method = { RequestMethod.GET })
	public AmxApiResponse<CardTypeDto, Object> getCustomerCardTypeList()  {
		return branchRemittanceClient.getCustomerCardTypeList();
	}
	
	@RequestMapping(value = "/api/remitt/card_type/update", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustomerCardType(@RequestParam BigDecimal chequeBankId,
			@RequestParam BigDecimal cardTypeId, @RequestParam String nameOnCard) {
		return branchRemittanceClient.updateCustomerCardType(chequeBankId, cardTypeId, nameOnCard);
	}
}
