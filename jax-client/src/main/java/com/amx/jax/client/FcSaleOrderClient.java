package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.FC_SALE_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.constant.ApiEndpoint.MetaApi;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.CustomerRatingDTO;
import com.amx.amxlib.meta.model.FxExchangeRateDto;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.PurposeOfTransactionDto;
import com.amx.amxlib.model.request.FcSaleOrderTransactionRequestModel;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.FcSaleOrderApplicationResponseModel;
import com.amx.amxlib.model.response.FcSaleOrderDefaultResponseModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;
import com.amx.jax.rest.RestService;

/**
 * 
 * @author	:Rabil
 * Date		: 03/11/2018
 *
 */
@Component
public class FcSaleOrderClient extends AbstractJaxServiceClient {
	
	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

/*	@Autowired
	private JaxMetaInfo jaxMetaInfo;
*/
	@Autowired
	private ConverterUtility util;

	@Autowired
	RestService restService;
	
	/**
	 * 
	 * @return :to get the purpose of Trnx for FC Sale
	 */
	public AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx() {
		try {
			LOGGER.info("in getFcPurposeofTrnx :");
			String url = this.getBaseUrl() + FC_SALE_ENDPOINT + "/fc-purposeof-trnx/";
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
			String url = this.getBaseUrl() + FC_SALE_ENDPOINT + "/fc-currency-list/";
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
			String url = this.getBaseUrl() + FC_SALE_ENDPOINT + "/fc-sale-xrate/" + fxCurrencyId;
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
			String url = this.getBaseUrl() + FC_SALE_ENDPOINT + "/fc-sale-cal-xrate/";
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
			String url = this.getBaseUrl() + FC_SALE_ENDPOINT + "/fc-sale-default/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderDefaultResponseModel, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(FcSaleOrderTransactionRequestModel requestModel) throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			LOGGER.info(" Fc Sale create application :" + requestModel.toString());
			
			HttpEntity<FcSaleOrderTransactionRequestModel> requestEntity = new HttpEntity<FcSaleOrderTransactionRequestModel>(requestModel,getHeader());
			String url = this.getBaseUrl() + FC_SALE_ENDPOINT + "/fcsale-save-application/";
			return restService.ajax(url).post(requestEntity).as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>(){});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	/*
	 
	 public ApiResponse<RemittanceApplicationResponseModel> saveTransaction(
			RemittanceTransactionRequestModel transactionRequestModel)
			throws RemittanceTransactionValidationException, LimitExeededException {
		try {
			HttpEntity<RemittanceTransactionRequestModel> requestEntity = new HttpEntity<RemittanceTransactionRequestModel>(
					transactionRequestModel, getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-application/";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RemittanceApplicationResponseModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveTransaction : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
	
	
	
	
	
	---------
	
	public AmxApiResponse<CustomerRatingDTO, ?> saveCustomerRating(CustomerRatingDTO customerRatingDTO)
			throws RemittanceTransactionValidationException, LimitExeededException {

		try {
			HttpEntity<CustomerRatingDTO> requestEntity = new HttpEntity<CustomerRatingDTO>(customerRatingDTO,
					getHeader());

			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/save-customer-rating/";
			LOGGER.info(" Calling customer rating :" + customerRatingDTO.toString());
			return restService.ajax(url).post(requestEntity).asApiResponse(CustomerRatingDTO.class);
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in customer rating : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	 
	 
	 */
	
}

