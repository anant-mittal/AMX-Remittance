package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.FC_SALE_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApiEndpoint.MetaApi;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.FxExchangeRateDto;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.PurposeOfTransactionDto;
import com.amx.amxlib.model.response.ApiResponse;
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

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

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
	
	
}

