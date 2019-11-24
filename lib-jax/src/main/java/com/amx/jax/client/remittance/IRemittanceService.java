package com.amx.jax.client.remittance;

/**
 * @author rabil
 */
import java.math.BigDecimal;

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

import com.amx.jax.model.request.remittance.PlaceOrderRequestModel;
import com.amx.jax.model.request.remittance.PlaceOrderUpdateStatusDto;

import com.amx.jax.model.request.remittance.GetServiceApplicabilityRequest;

import com.amx.jax.model.request.remittance.RoutingPricingRequest;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.FlexFieldReponseDto;

import com.amx.jax.model.response.remittance.GsmPlaceOrderListDto;
import com.amx.jax.model.response.remittance.GsmSearchRequestParameter;

import com.amx.jax.model.response.remittance.GetServiceApplicabilityResponse;

import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.ParameterDetailsResponseDto;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.model.response.remittance.PaymentModeDto;
import com.amx.jax.model.response.remittance.RatePlaceOrderInquiryDto;
import com.amx.jax.model.response.remittance.RemittanceDeclarationReportDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;

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
		public static final String BR_DECLARATION_REPORT			=PREFIX + "/get-declartion-report/";
		public static final String BR_RECEIPT_ON_EMAIL				=PREFIX + "/send-receipt-on-email/";
		public static final String BR_REMITTANCE_GET_ROUTING_PRICING_RATE = PREFIX + "/get-routing-pricing-exchrate/";
		public static final String BR_REMITTANCE_GET_FLEX_FIELDS = PREFIX + "/get-flex-field/";
		public static final String BR_REMITTANCE_PAYMENT_LINK = PREFIX + "/payment-link/";
		public static final String BR_REMITTANCE_VALIDATE_PAY_LINK = PREFIX + "/validate-payment-link/";

		public static final String GET_SERVICE_APPLICABILITY = PREFIX + "/get-service-applicability/";

		
		public static final String BR_REMITTANCE_GET_GIFT_PACKAGE = PREFIX + "/get-gift-package/";
		public static final String BR_REMITTANCE_SAVE_PLACE_ORDER = PREFIX + "/save-place-order-appl/";
		public static final String BR_REMITTANCE_FETCH_PLACE_ORDER = PREFIX + "/rate-place-order-inq/";
		public static final String BR_REMITTANCE_UPDATE_PLACE_ORDER = PREFIX + "/update_rate-place-order/";
		public static final String BR_REMITTANCE_PLACE_ORDER_COUNT = PREFIX + "/rate-place-order-count/";
		public static final String BR_REMITTANCE_ACCEPT_PLACE_ORDER = PREFIX + "/accept-place-order/";
		
		
		
		
		
	}

	public static class Params {
		public static final String TRNX_DATE = "transactiondate";
		public static final String BANK_ID = "bankId";
		public static final String STAFF_USERNAME = "staffUserName";
		public static final String STAFF_PASSWORD = "staffPassword";
		public static final String BENE_RELATION_SHIP_ID = "beneRelationshipId";
		public static final String SERVICE_MASTER_ID = "serviceMasterId";
		public static final String APPLICATION_ID = "remittanceApplicationId";
		public static final String COLLECTION_DOC_NO = "collectionDocNo";
		public static final String COLLECTION_DOC_FY = "collectionDocYear";
		public static final String COLLECTION_DOC_CODE = "collectionDocCode";
		public static final String LOCAL_AMOUNT = "localAmount";
		public static final String FOREIGN_AMOUNT = "foreignAmount";
		public static final String ROUTING_COUNTRY_ID="routingcountryId";
		public static final String COUNTRY_BRANCH_ID="countryBranchId"; 
		public static final String LINK_ID="linkId";
		public static final String VERIFICATION_CODE="verificationCode";
		public static final String RATE_PLACE_ORDER_ID="ratePlaceOrderId";
		
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
	AmxApiResponse<PaymentModeDto, Object> fetchModeOfPayment();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks();
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerBankNames(BigDecimal bankId);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<ResourceDTO, Object> fetchPosBanks();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination();

	AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(CustomerBankRequest customerBank);
	
	AmxApiResponse<BoolRespModel, Object> validationStaffCredentials(String staffUserName, String staffPassword);
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.SERVICE_NOT_FOUND,JaxError.ROUTING_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_BRANCH_NOT_FOUND,JaxError.DELIVERY_MODE_NOT_FOUND,JaxError.REMITTANCE_MODE_NOT_FOUND})
	AmxApiResponse<RoutingResponseDto,Object> getRoutingSetupDeatils(BigDecimal beneRelaId);
	
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.SERVICE_NOT_FOUND,JaxError.ROUTING_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_COUNTRY_NOT_FOUND,JaxError.ROUTING_BANK_BRANCH_NOT_FOUND,JaxError.DELIVERY_MODE_NOT_FOUND,JaxError.REMITTANCE_MODE_NOT_FOUND})
	AmxApiResponse<RoutingResponseDto,Object> getRoutingDetailsByServiceId(BigDecimal beneRelaId,BigDecimal serviceMasterId);
	
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND})
	AmxApiResponse<AdditionalExchAmiecDto,Object> getPurposeOfTrnx(BigDecimal beneRelaId,BigDecimal routingcountryId);
	
	@ApiJaxStatus({ JaxError.INVALID_AMOUNT , JaxError.EXCHANGE_RATE_NOT_FOUND, JaxError.DATA_NOT_FOUND})
	AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(BranchRemittanceGetExchangeRateRequest request);
	
	
	@ApiJaxStatus({ JaxError.INVALID_AMOUNT , JaxError.EXCHANGE_RATE_NOT_FOUND, JaxError.DATA_NOT_FOUND,JaxError.INVALID_COLLECTION_DOCUMENT_NO,JaxError.INVALID_REMITTANCE_DOCUMENT_NO})
	AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(BranchRemittanceRequestModel remittanceRequestModel);
	AmxApiResponse<BranchRemittanceApplResponseDto, Object> deleteFromShoppingCart(BigDecimal remittanceApplicationId);
	
	
	@ApiJaxStatus({JaxError.INVALID_COLLECTION_DOCUMENT_NO, JaxError.DATA_NOT_FOUND})
	AmxApiResponse<RemittanceDeclarationReportDto,Object> fetchCustomerDeclarationReport(BigDecimal collectionDocNo,BigDecimal collectionDocYear ,BigDecimal collectionDocCode);
	
	
	AmxApiResponse<BoolRespModel, Object> sendReceiptOnEmail(BigDecimal collectionDocNo,BigDecimal collectionDocYear ,BigDecimal collectionDocCode);

	
	@ApiJaxStatus({ JaxError.INVALID_AMOUNT , JaxError.EXCHANGE_RATE_NOT_FOUND, JaxError.DATA_NOT_FOUND})
	AmxApiResponse<DynamicRoutingPricingResponse, Object> getDynamicRoutingPricing(RoutingPricingRequest routingPricingRequest);
	
	@ApiJaxStatus({JaxError.DATA_NOT_FOUND})
	AmxApiResponse<FlexFieldReponseDto,Object> getFlexField(BranchRemittanceGetExchangeRateRequest request);

	AmxApiResponse<GetServiceApplicabilityResponse, Object> getServiceApplicability(GetServiceApplicabilityRequest request);
	
	AmxApiResponse<PaymentLinkRespDTO, Object> createAndSendPaymentLink();
	
	@ApiJaxStatus({JaxError.VERIFICATION_CODE_MISMATCH})
	AmxApiResponse<PaymentLinkRespDTO, Object> validatePayLink(BigDecimal linkId,String verificationCode);

	@ApiJaxStatus({JaxError.NO_RECORD_FOUND})
	AmxApiResponse<ParameterDetailsResponseDto, Object> getGiftService(BigDecimal beneRelaId);
	
	
	AmxApiResponse<BoolRespModel, Object> savePlaceOrderApplication(PlaceOrderRequestModel placeOrderRequestModel);
	
	AmxApiResponse<RatePlaceOrderInquiryDto, Object> fetchPlaceOrderInquiry(BigDecimal countryBranchId);
	
	
	AmxApiResponse<BoolRespModel, Object>  updateRatePlaceOrder(PlaceOrderUpdateStatusDto dto);
	
	
	AmxApiResponse<GsmPlaceOrderListDto,Object>  getCountryWisePlaceOrderCount(GsmSearchRequestParameter requestParameter);
	
	AmxApiResponse<DynamicRoutingPricingDto,Object> acceptPlaceOrderByCustomer(BigDecimal ratePlaceOrderId);
	
}



