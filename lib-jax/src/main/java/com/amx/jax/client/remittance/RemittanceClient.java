package com.amx.jax.client.remittance;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.fx.FcSaleOrderClient;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.rest.RestService;

public class RemittanceClient  implements IRemittanceService{
	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

	@Autowired
	RestService restService;
	
	@Autowired
	AppConfig appConfig;

	/**
	 * fetch customer shopping cart application
	 * 
	 */
	@Override
	public AmxApiResponse<CustomerShoppingCartDto, Object> fetchCustomerShoppingCart() {
		try {
			LOGGER.debug("in fetchCustomerShoppingCart :");
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_SHOPPING_CART).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerShoppingCartDto, Object>>() {
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
	public AmxApiResponse<String, Object> fetchCustomerBankNames(BigDecimal bankId) {
		try {
			LOGGER.debug("in fetchCustomerBankNames :"+bankId);
			return restService.ajax(appConfig.getJaxURL() + Path.BR_REMITTANCE_BANK_CUSTOMER_NAMES).meta(new JaxMetaInfo())
					.queryParam(Params.BANK_ID, bankId)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
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
	
}

