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
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.model.response.remittance.RemittanceDeclarationReportDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.rest.RestService;

@Component
public class RemittanceClient  implements IRemittanceService{
	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

	@Autowired
	RestService restService;
	
	@Autowired
	AppConfig appConfig;
	
	
	/**
	 * Save the application
	 */
	
	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(BranchRemittanceApplRequestModel requestModel) {
		try {
			LOGGER.debug("in saveBranchRemittanceApplication :"+requestModel);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SAVE_APPL).meta(new JaxMetaInfo())
					.post(requestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceApplResponseDto,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveCustomerBankDetails : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}
		

	
	

	/**
	 * fetch customer shopping cart application
	 * 
	 */
	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> fetchCustomerShoppingCart() {
		try {
			LOGGER.debug("in fetchCustomerShoppingCart :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SHOPPING_CART).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceApplResponseDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchCustomerShoppingCart : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * fetch mode of payment
	 * 
	 */
	@Override
	public AmxApiResponse<PaymentModeOfPaymentDto, Object> fetchModeOfPayment() {
		try {
			LOGGER.debug("in fetchModeOfPayment :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_MODE_OF_PAYMENT).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<PaymentModeOfPaymentDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchModeOfPayment : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * fetch local bank list
	 * 
	 */
	@Override
	public AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks() {
		try {
			LOGGER.debug("in fetchLocalBanks :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_LOCAL_BANKS).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<LocalBankDetailsDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchLocalBanks : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * fetch customer Banks added
	 * 
	 */
	@Override
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks() {
		try {
			LOGGER.debug("in fetchCustomerLocalBanks :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_CUSTOMER_BANKS).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerBankDetailsDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchCustomerLocalBanks : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * fetch customer Banks Names by bank Id and Customer Id
	 * 
	 */
	@Override
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerBankNames(BigDecimal bankId) {
		try {
			LOGGER.debug("in fetchCustomerBankNames :"+bankId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_BANK_CUSTOMER_NAMES).meta(new JaxMetaInfo())
					.queryParam(Params.BANK_ID, bankId)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerBankDetailsDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchCustomerBankNames : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * fetch pos banks list
	 * 
	 */
	@Override
	public AmxApiResponse<ResourceDTO, Object> fetchPosBanks() {
		try {
			LOGGER.debug("in fetchPosBanks :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_POS_BANKS).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ResourceDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchPosBanks : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * fetch pay in stock local currency
	 * 
	 */
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination() {
		try {
			LOGGER.debug("in fetchLocalCurrencyDenomination :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_LOCAL_CURRENCY_DENOMINATION).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchLocalCurrencyDenomination : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * fetch pay in stock local currency for refund
	 * 
	 */
	@Override
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination() {
		try {
			LOGGER.debug("in fetchLocalCurrencyRefundDenomination :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_LOCAL_CURRENCY_REFUND_DENOMINATION).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchLocalCurrencyRefundDenomination : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/**
	 * save the customer bank details
	 * 
	 */
	@Override
	public AmxApiResponse<BoolRespModel, Object> saveCustomerBankDetails(List<CustomerBankRequest> customerBank) {
		try {
			LOGGER.debug("in saveCustomerBankDetails :"+customerBank);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SAVE_CUSTOMER_BANKS).meta(new JaxMetaInfo())
					.post(customerBank)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveCustomerBankDetails : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> validationStaffCredentials(String staffUserName,String staffPassword) {
		try {
			LOGGER.debug("in validationStaffCredentials :"+staffUserName + " " + staffPassword);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_VALIDATE_STAFF_CREDENTIALS).meta(new JaxMetaInfo())
					.queryParam(Params.STAFF_USERNAME, staffUserName).meta(new JaxMetaInfo())
					.queryParam(Params.STAFF_PASSWORD, staffPassword)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in validationStaffCredentials : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}





	@Override
	public AmxApiResponse<RoutingResponseDto, Object> getRoutingSetupDeatils(BigDecimal beneRelaId) {
		try {
			LOGGER.debug("in getRoutingSetupDeatils :"+beneRelaId );
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_ROUTING).meta(new JaxMetaInfo())
					.queryParam(Params.BENE_RELATION_SHIP_ID, beneRelaId).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<RoutingResponseDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in validationStaffCredentials : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-cat
	}





	@Override
	public AmxApiResponse<RoutingResponseDto, Object> getRoutingDetailsByServiceId(BigDecimal beneRelaId,BigDecimal serviceMasterId) {
		try {
		LOGGER.debug("in getRoutingSetupDeatils :"+beneRelaId );
		return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_ROUTING_BY_SERVICE).meta(new JaxMetaInfo())
				.queryParam(Params.BENE_RELATION_SHIP_ID, beneRelaId).meta(new JaxMetaInfo())
				.queryParam(Params.SERVICE_MASTER_ID, serviceMasterId)
				.post()
				.as(new ParameterizedTypeReference<AmxApiResponse<RoutingResponseDto, Object>>() {
				});
	} catch (Exception e) {
		LOGGER.error("exception in validationStaffCredentials : ", e);
		return JaxSystemError.evaluate(e);
	} // end of try-cat
	}





	@Override
	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeOfTrnx(BigDecimal beneRelaId) {
		try {
			LOGGER.debug("in getRoutingSetupDeatils :"+beneRelaId );
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_PURPOSE_OF_TRNX).meta(new JaxMetaInfo())
					.queryParam(Params.BENE_RELATION_SHIP_ID, beneRelaId).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<AdditionalExchAmiecDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in validationStaffCredentials : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-cat
	}



	@Override
	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(
			BranchRemittanceGetExchangeRateRequest request) {
		try {
			LOGGER.debug("in getExchaneRate :" + request);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_GET_EXCHANGE_RATE)
					.meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getExchaneRate : ", e);
			return JaxSystemError.evaluate(e);
		}
	}





	@Override
	public AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(BranchRemittanceRequestModel remittanceRequestModel) {
		try {
			LOGGER.debug("in saveRemittanceTransaction :" + remittanceRequestModel);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SAVE_TRANSACTION)
					.meta(new JaxMetaInfo()).post(remittanceRequestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<RemittanceResponseDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getExchaneRate : ", e);
			return JaxSystemError.evaluate(e);
		}
		
	}





	@Override
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> deleteFromShoppingCart(BigDecimal remittanceApplicationId) {
		try {
			LOGGER.debug("in deleteFromShoppingCart :"+remittanceApplicationId );
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_DELETE_APPLICATION).meta(new JaxMetaInfo())
					.queryParam(Params.APPLICATION_ID, remittanceApplicationId).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BranchRemittanceApplResponseDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in deleteFromShoppingCart : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-cat
		
	}





	@Override
	public AmxApiResponse<RemittanceDeclarationReportDto, Object> fetchCustomerDeclarationReport(BigDecimal collectionDocNo, BigDecimal collectionDocYear,
			BigDecimal collectionDocCode) {
		try {
			LOGGER.debug("in fetchCustomerDeclarationReport :"+collectionDocNo );
			return restService.ajax(appConfig.getJaxURL() + Path.BR_DECLARATION_REPORT).meta(new JaxMetaInfo())
					.queryParam(Params.COLLECTION_DOC_NO, collectionDocNo).meta(new JaxMetaInfo())
					.queryParam(Params.COLLECTION_DOC_FY, collectionDocYear)
					.queryParam(Params.COLLECTION_DOC_CODE, collectionDocCode)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<RemittanceDeclarationReportDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in deleteFromShoppingCart : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-cat
	}





	@Override
	public AmxApiResponse<BoolRespModel, Object> deActivateOnlineApplication() {
		try {
			return restService.ajax(appConfig.getJaxURL() + Path.BR_DEACTIVE_ONLINE_APPL).meta(new JaxMetaInfo())
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("");
			return JaxSystemError.evaluate(e);
		} // end of try-catch
		
	}

}

