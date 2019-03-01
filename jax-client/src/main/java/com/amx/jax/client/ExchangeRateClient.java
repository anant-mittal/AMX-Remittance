package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.EXCHANGE_RATE_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.rest.RestService;

@Component
public class ExchangeRateClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(ExchangeRateClient.class);

	@Autowired
	private RestService restService;

	public ApiResponse<ExchangeRateResponseModel> getExchangeRate(BigDecimal fromCurrency, BigDecimal toCurrency,
			BigDecimal amount, BigDecimal bankId, BigDecimal beneBankCountryId) throws ResourceNotFoundException, InvalidInputException {
		try {
			String endpoint = EXCHANGE_RATE_ENDPOINT + "/online/";
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("fromCurrency=").append(fromCurrency);
			sb.append("&").append("toCurrency=").append(toCurrency);
			sb.append("&").append("amount=").append(amount);
			if (bankId != null) {
				sb.append("&").append("bankId=").append(bankId);
			}
			if (beneBankCountryId != null) {
				sb.append("&").append("beneBankCountryId=").append(beneBankCountryId);
			}
			String getExchangeRateUrl = this.getBaseUrl() + endpoint + sb.toString();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			LOGGER.info("calling getExchangeRate api: " + getExchangeRateUrl);
			return restService.ajax(getExchangeRateUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<ExchangeRateResponseModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getExchangeRate : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> setExchangeRate(String toCurrency, BigDecimal amount)
			throws ResourceNotFoundException, InvalidInputException {
		try {
			String endpoint = EXCHANGE_RATE_ENDPOINT + "/online/";
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("quoteName=").append(toCurrency);
			sb.append("&").append("value=").append(amount);

			String getExchangeRateUrl = this.getBaseUrl() + endpoint + sb.toString();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			LOGGER.info("calling getExchangeRate api: " + getExchangeRateUrl);
			return restService.ajax(getExchangeRateUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in setExchangeRate : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * Min Max Exchange rate client call
	 * 
	 */
	public ApiResponse<MinMaxExRateDTO> getMinMaxExchangeRate()
			throws ResourceNotFoundException, InvalidInputException {
		try {
			LOGGER.info("Get in Min Max Exchange Rate Client ");

			String url = this.getBaseUrl() + EXCHANGE_RATE_ENDPOINT + "/min-max/exchangerate/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<MinMaxExRateDTO>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in minMaxExRate : ", e);
			throw new JaxSystemError();
		}
	}
	
	public ApiResponse<BooleanResponse> setExchangeRatePlaceorder(String toCurrency, BigDecimal bankId, BigDecimal amount)
			throws ResourceNotFoundException, InvalidInputException {
		try {
			String endpoint = EXCHANGE_RATE_ENDPOINT + "/online/placeorder";
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("quoteName=").append(toCurrency);
			sb.append("&").append("bankId=").append(bankId);
			sb.append("&").append("value=").append(amount);

			String getExchangeRateUrl = this.getBaseUrl() + endpoint + sb.toString();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			LOGGER.info("calling getExchangeRate api: " + getExchangeRateUrl);
			return restService.ajax(getExchangeRateUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in setExchangeRate : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
}
