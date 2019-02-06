package com.amx.jax.branchremittance.controller;
/**
 * @author rabil
 * @date  17/01/2019 
 */

import java.math.BigDecimal;
import java.util.List;

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
import com.amx.jax.branchremittance.service.BranchRemittanceService;
import com.amx.jax.client.remittance.IRemittanceService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;

@RestController
public class BranchRemittanceController implements IRemittanceService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MetaData metaData;

	@Autowired
	BranchRemittanceService branchRemitService;

	@RequestMapping(value = Path.BR_REMITTANCE_SAVE_APPL, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(
			@RequestBody BranchRemittanceApplRequestModel requestModel) {
		logger.info("saveBranchRemittanceApplication :" + requestModel);
		return branchRemitService.saveBranchRemittanceApplication(requestModel);
	}

	/**
	 * fetch customer shopping cart application
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_SHOPPING_CART, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<CustomerShoppingCartDto, Object> fetchCustomerShoppingCart() {
		logger.info("fetchCustomerShoppingCart");
		return branchRemitService.fetchCustomerShoppingCart();
	}

	/**
	 * fetch mode of payment
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_MODE_OF_PAYMENT, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<PaymentModeOfPaymentDto, Object> fetchModeOfPayment() {
		logger.info("fetchModeOfPayment");
		return branchRemitService.fetchModeOfPayment();
	}

	/**
	 * fetch local bank list
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_LOCAL_BANKS, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks() {
		logger.info("fetchLocalBanks");
		return branchRemitService.fetchLocalBanks();
	}

	/**
	 * fetch customer Banks added
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_CUSTOMER_BANKS, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks() {
		logger.info("fetchCustomerLocalBanks");
		return branchRemitService.fetchCustomerLocalBanks();
	}

	/**
	 * fetch customer Banks Names by bank Id and Customer Id
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_BANK_CUSTOMER_NAMES, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<String, Object> fetchCustomerBankNames(
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
		logger.info("fetchPosBanks");
		return branchRemitService.fetchPosBanks();
	}

	/**
	 * fetch pay in stock local currency
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_LOCAL_CURRENCY_DENOMINATION, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination() {
		logger.info("fetchLocalCurrencyDenomination");
		return branchRemitService.fetchLocalCurrencyDenomination();
	}

	/**
	 * fetch pay in stock local currency for refund
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_LOCAL_CURRENCY_REFUND_DENOMINATION, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination() {
		logger.info("fetchLocalCurrencyRefundDenomination");
		return branchRemitService.fetchLocalCurrencyRefundDenomination();
	}

	/**
	 * save the customer bank details
	 * 
	 */
	@RequestMapping(value = Path.BR_REMITTANCE_SAVE_CUSTOMER_BANKS, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(
			@RequestBody List<CustomerBankRequest> customerBank) {
		logger.info("saveCustomerBankDetails" + customerBank);
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
		logger.info("getRoutingSetupDeatils :" + beneRelaId + "\t serviceMasterId :" + serviceMasterId);
		return branchRemitService.getRoutingDetailsByServiceId(beneRelaId, serviceMasterId);
	}

	@RequestMapping(value = Path.BR_REMITTANCE_PURPOSE_OF_TRNX, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeOfTrnx(
			@RequestParam(value = Params.BENE_RELATION_SHIP_ID) BigDecimal beneRelaId) {
		logger.info("getPurposeOfTrnx :" + beneRelaId);
		return branchRemitService.getPurposeOfTrnx(beneRelaId);
	}

}
