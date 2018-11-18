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
import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.FxExchangeRateDto;
import com.amx.jax.model.response.PurposeOfTransactionDto;
import com.amx.jax.model.response.ShippingAddressDto;
import com.amx.jax.model.response.ShoppingCartDetailsDto;
import com.amx.jax.rest.RestService;



@Component
public class FcSaleOrderClient implements IJaxService{
	
	private static final String FC_SALE_ENDPOINT = "/fc/sale/";
	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);


	@Autowired
	RestService restService;
	
	@Autowired
	AppConfig appConfig;

	
	/**
	 * 
	 * @return :to get the purpose of Trnx for FC Sale
	 */
	public AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx() {
		try {
			LOGGER.info("in getFcPurposeofTrnx :");
			String url = appConfig.getJaxURL() +  FC_SALE_ENDPOINT + "/fc-purposeof-trnx/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<PurposeOfTransactionDto, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	/**
	 * 
	 * @return :to get all the  FC Sale currency 
	 */
	
	
	public AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList() {
		try {
			LOGGER.info("in getFcPurposeofTrnx :");
			String url = appConfig.getJaxURL()+ FC_SALE_ENDPOINT + "/fc-currency-list/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * 
	 * @return : fccurrency  rate 
	 * 
	 */

	public AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(BigDecimal fxCurrencyId) {
		try {
			LOGGER.info("in getFcXRate :"+fxCurrencyId);
			String url = appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fc-sale-xrate/" + fxCurrencyId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<FxExchangeRateDto, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	
	/**
	 * 
	 * @return : to get the exchange rate
	 * 
	 */

	public AmxApiResponse<FxExchangeRateDto, Object> calculateXRate(BigDecimal fxCurrencyId,BigDecimal fcAmount) {
		try {
			LOGGER.info("in getFcXRate :"+fxCurrencyId+"\t fcAmount :"+fcAmount);
			String url = appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fc-sale-cal-xrate/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("fxCurrencyId", fxCurrencyId).queryParam("fcAmount", fcAmount);
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<FxExchangeRateDto, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	public AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi() {
		try {
			LOGGER.info("getFcSaleDefaultApi client :");
			String url =appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fc-sale-default/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderDefaultResponseModel, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(FcSaleOrderTransactionRequestModel requestModel) throws Exception {
		try {
			LOGGER.info(" Fc Sale create application :" + requestModel.toString());
			HttpEntity<FcSaleOrderTransactionRequestModel> requestEntity = new HttpEntity<FcSaleOrderTransactionRequestModel>(requestModel,getHeader());
			String url = appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fcsale-save-application/";
			return restService.ajax(url).post(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	
	
	public AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress() {
		try {
			LOGGER.info("getFcSale shipping Address client :");
			String url = appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fc-sale-address/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<ShippingAddressDto, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	
	
	/** 
	 * @purpose : Save FC Sale shipping address
	  				CustomerShippingAddressRequestModel 
	 **/
	public AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(CustomerShippingAddressRequestModel requestModel) throws Exception {
		try {
			LOGGER.info(" Fc Sale shipping address save :" + requestModel.toString());
			HttpEntity<CustomerShippingAddressRequestModel> requestEntity = new HttpEntity<CustomerShippingAddressRequestModel>(requestModel,getHeader());
			String url = appConfig.getJaxURL() + FC_SALE_ENDPOINT + "/fc-save-shipping-addr/";
			return restService.ajax(url).post(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<CustomerShippingAddressRequestModel, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in shipping address save : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	
	
	

	/** @ To fetch the time slot **/
	public AmxApiResponse<String, Object> getTimeSlot(String  fxDate) {
		try {
			LOGGER.info("in getTime slot client :"+fxDate);
			String url = appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fc-sale-time-slot/" + fxDate;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	
	/** @ Remove cart from list by passing application id **/
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(BigDecimal applicationId) throws Exception {
		try {
			LOGGER.info(" Fc Sale create application :" + applicationId);
			String url = appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fc-sale-remove-item/"+applicationId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).post(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	
	
	/** @ To fetch the time slot **/
	public AmxApiResponse<ShoppingCartDetailsDto, Object> fetchShoppingCartList() {
		try {
			LOGGER.info("in fetchShoppingCartList  client :");
			String url = appConfig.getJaxURL()  + FC_SALE_ENDPOINT + "/fc-sale-shopping-cart/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<ShoppingCartDetailsDto, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
}

