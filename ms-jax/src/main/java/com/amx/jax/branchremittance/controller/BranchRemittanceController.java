package com.amx.jax.branchremittance.controller;
/**
 * @author rabil
 * @date  17/01/2019 
 */

import java.math.BigDecimal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branchremittance.service.BranchRemittanceExchangeRateService;
import com.amx.jax.branchremittance.service.BranchRemittanceService;
import com.amx.jax.client.remittance.IRemittanceService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.request.remittance.RoutingPricingRequest;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.FlexFieldReponseDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.model.response.remittance.PaymentModeDto;
import com.amx.jax.model.response.remittance.RemittanceDeclarationReportDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;

@RestController
public class BranchRemittanceController implements IRemittanceService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MetaData metaData;
	@Autowired
	BranchRemittanceService branchRemitService;
	@Autowired
	BranchRemittanceExchangeRateService branchRemittanceExchangeRateService;

	@RequestMapping(value = Path.BR_REMITTANCE_SAVE_APPL, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(
			@Valid @RequestBody BranchRemittanceApplRequestModel requestModel) {
		logger.info("saveBranchRemittanceApplication :" + requestModel);
		return branchRemitService.saveBranchRemittanceApplication(requestModel);
	}

	/**
	 * fetch customer shopping cart application
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_SHOPPING_CART, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> fetchCustomerShoppingCart() {
		logger.debug("fetchCustomerShoppingCart");
		return branchRemitService.fetchCustomerShoppingCart();
	}

	/**
	 * fetch mode of payment
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_MODE_OF_PAYMENT, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<PaymentModeDto, Object> fetchModeOfPayment() {
		logger.debug("fetchModeOfPayment");
		return branchRemitService.fetchModeOfPayment();
	}

	/**
	 * fetch local bank list
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_LOCAL_BANKS, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks() {
		logger.debug("fetchLocalBanks");
		return branchRemitService.fetchLocalBanks();
	}

	/**
	 * fetch customer Banks added
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_CUSTOMER_BANKS, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks() {
		logger.debug("fetchCustomerLocalBanks");
		return branchRemitService.fetchCustomerLocalBanks();
	}

	/**
	 * fetch customer Banks Names by bank Id and Customer Id
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_BANK_CUSTOMER_NAMES, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerBankNames(
			@RequestParam(value = "bankId", required = true) BigDecimal bankId) {
		logger.info("fetchCustomerBankNames");
		return branchRemitService.fetchCustomerBankNames(bankId);
	}

	/**
	 * fetch pos banks list
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_POS_BANKS, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<ResourceDTO, Object> fetchPosBanks() {
		logger.debug("fetchPosBanks");
		return branchRemitService.fetchPosBanks();
	}

	/**
	 * fetch pay in stock local currency
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_LOCAL_CURRENCY_DENOMINATION, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination() {
		logger.debug("fetchLocalCurrencyDenomination");
		return branchRemitService.fetchLocalCurrencyDenomination();
	}

	/**
	 * fetch pay in stock local currency for refund
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_LOCAL_CURRENCY_REFUND_DENOMINATION, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination() {
		logger.debug("fetchLocalCurrencyRefundDenomination");
		return branchRemitService.fetchLocalCurrencyRefundDenomination();
	}

	/**
	 * save the customer bank details
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_SAVE_CUSTOMER_BANKS, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(
			@RequestBody CustomerBankRequest customerBank) {
		logger.debug("saveCustomerBankDetails" + customerBank);
		BoolRespModel result = branchRemitService.saveCustomerBankDetails(customerBank);
		return AmxApiResponse.build(result);
	}

	/**
	 * validate staff user name and password
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_VALIDATE_STAFF_CREDENTIALS, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> validationStaffCredentials(
			@RequestParam(value = "staffUserName", required = true) String staffUserName,
			@RequestParam(value = "staffPassword", required = true) String staffPassword) {
		BoolRespModel result = branchRemitService.validationStaffCredentials(staffUserName, staffPassword);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.BR_REMITTANCE_ROUTING, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<RoutingResponseDto, Object> getRoutingSetupDeatils(
			@RequestParam(value = Params.BENE_RELATION_SHIP_ID, required = true) BigDecimal beneRelaId) {
		logger.info("getRoutingSetupDeatils :" + beneRelaId);
		return branchRemitService.getRoutingDetails(beneRelaId);
	}

	@RequestMapping(value = Path.BR_REMITTANCE_ROUTING_BY_SERVICE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<RoutingResponseDto, Object> getRoutingDetailsByServiceId(
			@RequestParam(value = Params.BENE_RELATION_SHIP_ID, required = true) BigDecimal beneRelaId,
			@RequestParam(value = "serviceMasterId", required = true) BigDecimal serviceMasterId) {
		logger.debug("getRoutingSetupDeatils :" + beneRelaId + "\t serviceMasterId :" + serviceMasterId);
		return branchRemitService.getRoutingDetailsByServiceId(beneRelaId, serviceMasterId);
	}

	@RequestMapping(value = Path.BR_REMITTANCE_PURPOSE_OF_TRNX, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeOfTrnx(
			@RequestParam(value = Params.BENE_RELATION_SHIP_ID,required=true) BigDecimal beneRelaId,@RequestParam(value = Params.ROUTING_COUNTRY_ID,required=true) BigDecimal routingCountryId) {
		logger.debug("getPurposeOfTrnx :" + beneRelaId);
		return branchRemitService.getPurposeOfTrnx(beneRelaId,routingCountryId);
	}

	@RequestMapping(value = Path.BR_REMITTANCE_GET_EXCHANGE_RATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(
			@Valid @RequestBody BranchRemittanceGetExchangeRateRequest request) {
		logger.debug("getExchaneRate : " + request);
		return branchRemittanceExchangeRateService.getExchaneRate(request);
	}

	@RequestMapping(value=Path.BR_REMITTANCE_SAVE_TRANSACTION,method=RequestMethod.POST)
	@Override
	public AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(@RequestBody @Valid BranchRemittanceRequestModel remittanceRequestModel) {
		logger.debug("saveRemittanceTransaction : " + remittanceRequestModel);
		return branchRemitService.saveRemittanceTransaction(remittanceRequestModel);
	}
	
	
	@RequestMapping(value = Path.BR_REMITTANCE_DELETE_APPLICATION, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> deleteFromShoppingCart(@RequestParam(value = Params.APPLICATION_ID, required = true) BigDecimal remittanceApplicationId){
		logger.debug("deleteFromShoppingCart");
		return branchRemitService.deleteFromShoppingCart(remittanceApplicationId);
	}

	@RequestMapping(value = Path.BR_DECLARATION_REPORT, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<RemittanceDeclarationReportDto, Object> fetchCustomerDeclarationReport(BigDecimal collectionDocNo, BigDecimal collectionDocYear,BigDecimal collectionDocCode) {
		return branchRemitService.fetchCustomerDeclarationReport(collectionDocNo,collectionDocYear,collectionDocCode);
	}

	@RequestMapping(value=Path.BR_RECEIPT_ON_EMAIL,method=RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> sendReceiptOnEmail(BigDecimal collectionDocNo, BigDecimal collectionDocYear, BigDecimal collectionDocCode) {
		BoolRespModel result =  branchRemitService.sendReceiptOnEmail(collectionDocNo,collectionDocYear,collectionDocCode);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value=Path.BR_REMITTANCE_GET_ROUTING_PRICING_RATE,method=RequestMethod.POST)
	@Override
	public AmxApiResponse<DynamicRoutingPricingResponse, Object> getDynamicRoutingPricing(@RequestBody @Valid RoutingPricingRequest routingPricingRequest) {
		// TODO Auto-generated method stub
		return branchRemittanceExchangeRateService.getDynamicRoutingAndPricingResponse(routingPricingRequest);
	}

	@RequestMapping(value=Path.BR_REMITTANCE_GET_FLEX_FIELDS,method=RequestMethod.POST)
	@Override
	public AmxApiResponse<FlexFieldReponseDto, Object> getFlexField(@Valid @RequestBody BranchRemittanceGetExchangeRateRequest request) {
	logger.debug("getExchaneRate : " + request);
	return branchRemittanceExchangeRateService.getFlexField(request);
	}

	@RequestMapping(value = Path.BR_REMITTANCE_PAYMENT_LINK, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<PaymentLinkRespDTO, Object> createAndSendPaymentLink() {
		logger.info("Payment Link API Call ------ ");
		return branchRemitService.createAndSendPaymentLink();
	}

	@RequestMapping(value=Path.BR_REMITTANCE_VALIDATE_PAY_LINK,method=RequestMethod.POST)
	@Override
	public AmxApiResponse<PaymentLinkRespDTO, Object> validatePayLink(BigDecimal linkId, String verificationCode) {
		logger.info(" ------ Validate Payment Link API Call ------ ");
		return branchRemitService.validatePayLink(linkId, verificationCode);
	}
	
}
