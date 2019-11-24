package com.amx.jax.client.remittance;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.fx.FcSaleOrderClient;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.BranchRemittanceGetExchangeRateRequest;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.request.remittance.PlaceOrderRequestModel;
import com.amx.jax.model.request.remittance.PlaceOrderUpdateStatusDto;
import com.amx.jax.model.request.remittance.RoutingPricingRequest;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.FlexFieldReponseDto;
import com.amx.jax.model.response.remittance.GsmPlaceOrderListDto;
import com.amx.jax.model.response.remittance.GsmSearchRequestParameter;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.model.response.remittance.ParameterDetailsResponseDto;
import com.amx.jax.model.response.remittance.PaymentModeDto;

import com.amx.jax.model.response.remittance.RatePlaceOrderInquiryDto;

import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;

import com.amx.jax.model.response.remittance.RemittanceDeclarationReportDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;
import com.amx.jax.rest.RestService;

@Component
public class RemittanceClient implements IRemittanceService {
	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	/**
	 * Save the application
	 */

	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(
			BranchRemittanceApplRequestModel requestModel) {
		
			LOGGER.debug("in saveBranchRemittanceApplication :" + requestModel);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SAVE_APPL).meta(new JaxMetaInfo())
					.post(requestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceApplResponseDto, Object>>() {
					});
		
	}

	/**
	 * fetch customer shopping cart application
	 * 
	 */
	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> fetchCustomerShoppingCart() {
		
			LOGGER.debug("in fetchCustomerShoppingCart :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SHOPPING_CART).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceApplResponseDto, Object>>() {
					});
		
	}

	/**
	 * fetch mode of payment
	 * 
	 */
	@Override
	public AmxApiResponse<PaymentModeDto, Object> fetchModeOfPayment() {
		
			LOGGER.debug("in fetchModeOfPayment :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_MODE_OF_PAYMENT).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<PaymentModeDto, Object>>() {
					});
		
	}

	/**
	 * fetch local bank list
	 * 
	 */
	@Override
	public AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks() {
		
			LOGGER.debug("in fetchLocalBanks :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_LOCAL_BANKS).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<LocalBankDetailsDto, Object>>() {
					});
		
	}

	/**
	 * fetch customer Banks added
	 * 
	 */
	@Override
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks() {
		
			LOGGER.debug("in fetchCustomerLocalBanks :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_CUSTOMER_BANKS).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerBankDetailsDto, Object>>() {
					});
		
	}

	/**
	 * fetch customer Banks Names by bank Id and Customer Id
	 * 
	 */
	@Override
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerBankNames(BigDecimal bankId) {
		
			LOGGER.debug("in fetchCustomerBankNames :" + bankId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_BANK_CUSTOMER_NAMES)
					.meta(new JaxMetaInfo())
					.queryParam(Params.BANK_ID, bankId)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerBankDetailsDto, Object>>() {
					});
		
	}

	/**
	 * fetch pos banks list
	 * 
	 */
	@Override
	public AmxApiResponse<ResourceDTO, Object> fetchPosBanks() {
		
			LOGGER.debug("in fetchPosBanks :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_POS_BANKS).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ResourceDTO, Object>>() {
					});
		
	}

	/**
	 * fetch pay in stock local currency
	 * 
	 */
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination() {
		
			LOGGER.debug("in fetchLocalCurrencyDenomination :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_LOCAL_CURRENCY_DENOMINATION)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto, Object>>() {
					});
		
	}

	/**
	 * fetch pay in stock local currency for refund
	 * 
	 */
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination() {
		
			LOGGER.debug("in fetchLocalCurrencyRefundDenomination :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_LOCAL_CURRENCY_REFUND_DENOMINATION)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto, Object>>() {
					});
		
	}

	/**
	 * save the customer bank details
	 * 
	 */
	@Override
	//public AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(List<CustomerBankRequest> customerBank) {
	public AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(CustomerBankRequest customerBank) {
		
			LOGGER.debug("in saveCustomerBankDetails :" + customerBank);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SAVE_CUSTOMER_BANKS)
					.meta(new JaxMetaInfo())
					.post(customerBank)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> validationStaffCredentials(String staffUserName,
			String staffPassword) {
		
			LOGGER.debug("in validationStaffCredentials :" + staffUserName + " " + staffPassword);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_VALIDATE_STAFF_CREDENTIALS)
					.meta(new JaxMetaInfo())
					.queryParam(Params.STAFF_USERNAME, staffUserName).meta(new JaxMetaInfo())
					.queryParam(Params.STAFF_PASSWORD, staffPassword)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<RoutingResponseDto, Object> getRoutingSetupDeatils(BigDecimal beneRelaId) {
		
			LOGGER.debug("in getRoutingSetupDeatils :" + beneRelaId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_ROUTING).meta(new JaxMetaInfo())
					.queryParam(Params.BENE_RELATION_SHIP_ID, beneRelaId).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<RoutingResponseDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<RoutingResponseDto, Object> getRoutingDetailsByServiceId(BigDecimal beneRelaId,
			BigDecimal serviceMasterId) {
	
			LOGGER.debug("in getRoutingSetupDeatils :" + beneRelaId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_ROUTING_BY_SERVICE)
					.meta(new JaxMetaInfo())
					.queryParam(Params.BENE_RELATION_SHIP_ID, beneRelaId).meta(new JaxMetaInfo())
					.queryParam(Params.SERVICE_MASTER_ID, serviceMasterId)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<RoutingResponseDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeOfTrnx(BigDecimal beneRelaId,BigDecimal routingCountryId) {
		
			LOGGER.debug("in getRoutingSetupDeatils :" + beneRelaId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_PURPOSE_OF_TRNX).meta(new JaxMetaInfo())
					.queryParam(Params.BENE_RELATION_SHIP_ID, beneRelaId).meta(new JaxMetaInfo())
					.queryParam(Params.ROUTING_COUNTRY_ID, routingCountryId)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<AdditionalExchAmiecDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(
			BranchRemittanceGetExchangeRateRequest request) {
	
			LOGGER.debug("in getExchaneRate :" + request);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_GET_EXCHANGE_RATE)
					.meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(
			BranchRemittanceRequestModel remittanceRequestModel) {
		
			LOGGER.debug("in saveRemittanceTransaction :" + remittanceRequestModel);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SAVE_TRANSACTION)
					.meta(new JaxMetaInfo()).post(remittanceRequestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<RemittanceResponseDto, Object>>() {
					});

	}

	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> deleteFromShoppingCart(
			BigDecimal remittanceApplicationId) {
		
			LOGGER.debug("in deleteFromShoppingCart :" + remittanceApplicationId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_DELETE_APPLICATION)
					.meta(new JaxMetaInfo())
					.queryParam(Params.APPLICATION_ID, remittanceApplicationId).meta(new JaxMetaInfo())
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceApplResponseDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<RemittanceDeclarationReportDto, Object> fetchCustomerDeclarationReport(
			BigDecimal collectionDocNo, BigDecimal collectionDocYear,
			BigDecimal collectionDocCode) {
		
			LOGGER.debug("in fetchCustomerDeclarationReport :" + collectionDocNo);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_DECLARATION_REPORT).meta(new JaxMetaInfo())
					.queryParam(Params.COLLECTION_DOC_NO, collectionDocNo).meta(new JaxMetaInfo())
					.queryParam(Params.COLLECTION_DOC_FY, collectionDocYear)
					.queryParam(Params.COLLECTION_DOC_CODE, collectionDocCode)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<RemittanceDeclarationReportDto, Object>>() {
					});
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> sendReceiptOnEmail(BigDecimal collectionDocNo,
			BigDecimal collectionDocYear, BigDecimal collectionDocCode) {
		
			LOGGER.debug("in fetchCustomerDeclarationReport :" + collectionDocNo);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_RECEIPT_ON_EMAIL).meta(new JaxMetaInfo())
					.queryParam(Params.COLLECTION_DOC_NO, collectionDocNo).meta(new JaxMetaInfo())
					.queryParam(Params.COLLECTION_DOC_FY, collectionDocYear)
					.queryParam(Params.COLLECTION_DOC_CODE, collectionDocCode)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}



//getDynamicRoutingPricing
	@Override
	public AmxApiResponse<DynamicRoutingPricingResponse, Object> getDynamicRoutingPricing(RoutingPricingRequest routingPricingRequest) {
		
		LOGGER.debug("in fetchCustomerDeclarationReport :"+routingPricingRequest);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_GET_ROUTING_PRICING_RATE).meta(new JaxMetaInfo())
					.post(routingPricingRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<DynamicRoutingPricingResponse, Object>>() {
					});
		
	}


	@Override
	public AmxApiResponse<FlexFieldReponseDto, Object> getFlexField(BranchRemittanceGetExchangeRateRequest request) {
		
				LOGGER.debug("in getExchaneRate :" + request);
				return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_GET_FLEX_FIELDS)
						.meta(new JaxMetaInfo()).post(request)
						.as(new ParameterizedTypeReference<AmxApiResponse<FlexFieldReponseDto, Object>>() {
						});
			
	}

	@Override
	public AmxApiResponse<PaymentLinkRespDTO, Object> createAndSendPaymentLink() {
		try {
			LOGGER.debug("In Payment link Create Client :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_PAYMENT_LINK).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<PaymentLinkRespDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in Payment link Create Client : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<PaymentLinkRespDTO, Object> validatePayLink(BigDecimal linkId, String verificationCode) {

		try {
			LOGGER.debug("In Validate Payment link Client : " );
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_VALIDATE_PAY_LINK).meta(new JaxMetaInfo())
					.queryParam(Params.LINK_ID, linkId)
					.queryParam(Params.VERIFICATION_CODE, verificationCode)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<PaymentLinkRespDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in Validate Payment link Client :", e);
			return JaxSystemError.evaluate(e);
		}
	
	}


@Override
	public AmxApiResponse<ParameterDetailsResponseDto, Object> getGiftService(BigDecimal beneRelaId) {
		
		//beneRelationshipId
		
	
			LOGGER.debug("in getGiftService :" + beneRelaId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_GET_GIFT_PACKAGE).meta(new JaxMetaInfo())
					.queryParam(Params.BENE_RELATION_SHIP_ID, beneRelaId).meta(new JaxMetaInfo())
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ParameterDetailsResponseDto, Object>>() {
					});
		
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> savePlaceOrderApplication(PlaceOrderRequestModel placeOrderRequestModel) {
		return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SAVE_PLACE_ORDER).meta(new JaxMetaInfo())
				.post(placeOrderRequestModel)
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<RatePlaceOrderInquiryDto, Object> fetchPlaceOrderInquiry(BigDecimal countryBranchId) {
		return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_FETCH_PLACE_ORDER).meta(new JaxMetaInfo())
				.queryParam(Params.COUNTRY_BRANCH_ID, countryBranchId)
				.post()
				.as(new ParameterizedTypeReference<AmxApiResponse<RatePlaceOrderInquiryDto, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateRatePlaceOrder(PlaceOrderUpdateStatusDto placeOrderRequestUpdatDto) {
		return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_FETCH_PLACE_ORDER).meta(new JaxMetaInfo())
				.post(placeOrderRequestUpdatDto)
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<GsmPlaceOrderListDto, Object> getCountryWisePlaceOrderCount(GsmSearchRequestParameter requestParameter) {
		return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_PLACE_ORDER_COUNT).meta(new JaxMetaInfo())
				.post(requestParameter)
				.as(new ParameterizedTypeReference<AmxApiResponse<GsmPlaceOrderListDto, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<DynamicRoutingPricingDto, Object> acceptPlaceOrderByCustomer(BigDecimal ratePlaceOrderId) {
		return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_ACCEPT_PLACE_ORDER).meta(new JaxMetaInfo())
				.queryParam(Params.RATE_PLACE_ORDER_ID, ratePlaceOrderId).meta(new JaxMetaInfo())
				.post()
				.as(new ParameterizedTypeReference<AmxApiResponse<DynamicRoutingPricingDto, Object>>() {
				});
	
	}


}
