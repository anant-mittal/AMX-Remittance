package com.amx.jax.client;

/**
 * @author	:Rabil
 * @Date	: 03/11/2018
 * @Purpose	: Fx Order Delivery Client
 */
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.FxExchangeRateDto;
import com.amx.jax.model.response.PurposeOfTransactionDto;
import com.amx.jax.model.response.ShippingAddressDto;
import com.amx.jax.model.response.ShoppingCartDetailsDto;
import com.amx.jax.rest.RestService;

@Component
public class FcSaleOrderClient implements IFxOrderService {

	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	/**
	 * 
	 * @return :to get the purpose of Trnx for FC Sale
	 */
	@Override
	public AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx() {
		try {
			LOGGER.debug("in getFcPurposeofTrnx :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_PURPOSEOF_TRNX).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<PurposeOfTransactionDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	/**
	 * 
	 * @return :to get all the FC Sale currency
	 */

	@Override
	public AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList() {
		try {
			LOGGER.debug("in getFcPurposeofTrnx :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_CURRENCY_LIST).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	/**
	 * 
	 * @return : fccurrency rate
	 * 
	 */

	@Override
	public AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(BigDecimal fxCurrencyId) {
		try {
			LOGGER.debug("in getFcXRate :" + fxCurrencyId);
			String url = appConfig.getJaxURL() + Path.FC_SALE_XRATE + fxCurrencyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).meta(new JaxMetaInfo()).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<FxExchangeRateDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	/**
	 * 
	 * @return : to get the exchange rate
	 * 
	 */

	@Override
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> calculateXRate(BigDecimal fxCurrencyId,
			BigDecimal fcAmount) {
		try {
			LOGGER.debug("in getFcXRate :" + fxCurrencyId + "\t fcAmount :" + fcAmount);
			String url = appConfig.getJaxURL() + Path.FC_SALE_CAL_XRATE;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("fxCurrencyId", fxCurrencyId).queryParam("fcAmount", fcAmount);
			return restService.ajax(url).meta(new JaxMetaInfo()).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	@Override
	public AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi() {
		try {
			LOGGER.debug("getFcSaleDefaultApi client :");
			String url = appConfig.getJaxURL() + Path.FC_SALE_DEFAULT;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).meta(new JaxMetaInfo()).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderDefaultResponseModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	@Override
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(
			FcSaleOrderTransactionRequestModel requestModel) throws Exception {
		try {
			LOGGER.debug(" Fc Sale create application :" + requestModel.toString());
			HttpEntity<FcSaleOrderTransactionRequestModel> requestEntity = new HttpEntity<FcSaleOrderTransactionRequestModel>(
					requestModel, getHeader());
			String url = appConfig.getJaxURL() + Path.FCSALE_SAVE_APPLICATION;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	@Override
	public AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress() {
		try {
			LOGGER.debug("getFcSale shipping Address client :");
			String url = appConfig.getJaxURL() + Path.FC_SALE_ADDRESS;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).meta(new JaxMetaInfo()).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ShippingAddressDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	/**
	 * @purpose : Save FC Sale shipping address CustomerShippingAddressRequestModel
	 **/
	@Override
	public AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(
			CustomerShippingAddressRequestModel requestModel) throws Exception {
		try {
			LOGGER.debug(" Fc Sale shipping address save :" + requestModel.toString());
			HttpEntity<CustomerShippingAddressRequestModel> requestEntity = new HttpEntity<CustomerShippingAddressRequestModel>(
					requestModel, getHeader());
			String url = appConfig.getJaxURL() + Path.FC_SAVE_SHIPPING_ADDR;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerShippingAddressRequestModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in shipping address save : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	/** @ To fetch the time slot **/
	@Override
	public AmxApiResponse<String, Object> getTimeSlot(String fxDate) {
		try {
			LOGGER.debug("in getTime slot client :" + fxDate);
			String url = appConfig.getJaxURL() + Path.FC_SALE_TIME_SLOT + fxDate;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).meta(new JaxMetaInfo()).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	/** @ Remove cart from list by passing application id **/
	@Override
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(BigDecimal applicationId)
			throws Exception {
		try {
			LOGGER.debug(" Fc Sale create application :" + applicationId);
			String url = appConfig.getJaxURL() + Path.FC_SALE_REMOVE_ITEM + applicationId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).meta(new JaxMetaInfo()).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	/** @ To fetch the time slot **/
	@Override
	public AmxApiResponse<ShoppingCartDetailsDto, Object> fetchShoppingCartList() {
		try {
			LOGGER.debug("in fetchShoppingCartList  client :");
			String url = appConfig.getJaxURL() + Path.FC_SALE_SHOPPING_CART;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).meta(new JaxMetaInfo()).get(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ShoppingCartDetailsDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	@Override
	public AmxApiResponse<FcSaleApplPaymentReponseModel, Object> getSavePayNowApplication(
			FcSaleOrderPaynowRequestModel requestModel) throws Exception {
		try {
			LOGGER.debug(" Fc Sale create application :" + requestModel.toString());
			HttpEntity<FcSaleOrderPaynowRequestModel> requestEntity = new HttpEntity<FcSaleOrderPaynowRequestModel>(
					requestModel, getHeader());
			String url = appConfig.getJaxURL() + Path.FCSALE_SAVE_PAYNOW;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleApplPaymentReponseModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

}
