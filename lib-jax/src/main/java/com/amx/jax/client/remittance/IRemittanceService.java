package com.amx.jax.client.remittance;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;

public interface IRemittanceService extends  IJaxService {

	
	public static class Path {
		public static final String PREFIX = "/branch/remittance/";
		
		public static final String BR_REMITTANCE_SAVE_APPL = PREFIX + "/save-appl/";
		public static final String BR_REMITTANCE_SHOPPING_CART = PREFIX + "/shopping-cart/";
		public static final String BR_REMITTANCE_MODE_OF_PAYMENT = PREFIX + "/mode-of-payment/";
		public static final String BR_REMITTANCE_LOCAL_BANKS = PREFIX + "/local-banks/";
		public static final String BR_REMITTANCE_CUSTOMER_BANKS = PREFIX + "/customer-banks/";
		public static final String BR_REMITTANCE_BANK_CUSTOMER_NAMES = PREFIX + "/bank-customer-names/";
		public static final String BR_REMITTANCE_POS_BANKS = PREFIX + "/pos-banks/";
		public static final String BR_REMITTANCE_LOCAL_CURRENCY_DENOMINATION = PREFIX + "/local-currency-denomination/";
		public static final String BR_REMITTANCE_LOCAL_CURRENCY_REFUND_DENOMINATION = PREFIX + "/local-currency-refund-denomination/";
		public static final String BR_REMITTANCE_SAVE_CUSTOMER_BANKS = PREFIX + "/save-customer-banks/";
		public static final String BR_REMITTANCE_VALIDATE_STAFF_CREDENTIALS = PREFIX + "/validate-staff-credentails/";
		public static final String BR_REMITTANCE_ROUTING = PREFIX + "/get-routing-details/";
		public static final String BR_REMITTANCE_ROUTING_BY_SERVICE = PREFIX + "/get-routing-details-by-serviceid/";
		public static final String BR_REMITTANCE_PURPOSE_OF_TRNX = PREFIX + "/get-purpose-of-trnx/";
		public static final String BR_REMITTANCE_GET_EXCHANGE_RATE = PREFIX + "/get-exchrate/";
		public static final String BR_REMITTANCE_SAVE_TRANSACTION = PREFIX + "/save-remittance-transaction/";
		public static final String BR_REMITTANCE_DELETE_APPLICATION = PREFIX + "/delete-application/";
	}

	public static class Params {
		public static final String TRNX_DATE = "transactiondate";
		public static final String BANK_ID = "bankId";
		public static final String STAFF_USERNAME = "staffUserName";
		public static final String STAFF_PASSWORD = "staffPassword";
		public static final String BENE_RELATION_SHIP_ID = "beneRelationshipId";
		public static final String SERVICE_MASTER_ID = "serviceMasterId";
		public static final String APPLICATION_ID = "remittanceApplicationId";
	}
	
	
	
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_CUSTOMER_ID,
		JaxError.NULL_CURRENCY_ID,JaxError.NULL_EMPLOYEE_ID,JaxError.TRNX_NOT_ALLOWED_ON_YOUR_OWN_LOGIN,
		JaxError.BENE_ACCOUNT_TYPE_MISMATCH,
		JaxError.ROUTING_SETUP_ERROR,
		JaxError.EXCHANGE_RATE_ERROR})
	AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(BranchRemittanceApplRequestModel requestModel);
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_CUSTOMER_ID,JaxError.NULL_CURRENCY_ID})
	AmxApiResponse<BranchRemittanceApplResponseDto, Object> fetchCustomerShoppingCart();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<PaymentModeOfPaymentDto, Object> fetchModeOfPayment();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks();
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<String, Object> fetchCustomerBankNames(BigDecimal bankId);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<ResourceDTO, Object> fetchPosBanks();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination();

	AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(List<CustomerBankRequest> customerBank);

	AmxApiResponse<BoolRespModel, Object> validationStaffCredentials(String staffUserName, String staffPassword);
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.SERVICE_NOT_FOUND,JaxError.ROUTING_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_BRANCH_NOT_FOUND,JaxError.DELIVERY_MODE_NOT_FOUND,JaxError.REMITTANCE_MODE_NOT_FOUND})
	AmxApiResponse<RoutingResponseDto,Object> getRoutingSetupDeatils(BigDecimal beneRelaId);
	
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.SERVICE_NOT_FOUND,JaxError.ROUTING_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_BRANCH_NOT_FOUND,JaxError.DELIVERY_MODE_NOT_FOUND,JaxError.REMITTANCE_MODE_NOT_FOUND})
	AmxApiResponse<RoutingResponseDto,Object> getRoutingDetailsByServiceId(BigDecimal beneRelaId,BigDecimal serviceMasterId);
	
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<AdditionalExchAmiecDto,Object> getPurposeOfTrnx(BigDecimal beneRelaId);
	
	@ApiJaxStatus({ JaxError.INVALID_AMOUNT , JaxError.EXCHANGE_RATE_NOT_FOUND, JaxError.DATA_NOT_FOUND})
	AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(BranchRemittanceGetExchangeRateRequest request);
	
	
	@ApiJaxStatus({ JaxError.INVALID_AMOUNT , JaxError.EXCHANGE_RATE_NOT_FOUND, JaxError.DATA_NOT_FOUND,JaxError.INVALID_COLLECTION_DOCUMENT_NO,JaxError.INVALID_REMITTANCE_DOCUMENT_NO})
	AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(BranchRemittanceRequestModel remittanceRequestModel);

	AmxApiResponse<BranchRemittanceApplResponseDto, Object> deleteFromShoppingCart(BigDecimal remittanceApplicationId);

}



