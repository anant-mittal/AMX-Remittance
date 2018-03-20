package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.EXCHANGE_RATE_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;

@Component
public class ExchangeRateClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(ExchangeRateClient.class);

	public ApiResponse<ExchangeRateResponseModel> getExchangeRate(BigDecimal fromCurrency, BigDecimal toCurrency,
			BigDecimal amount, BigDecimal bankId) throws ResourceNotFoundException, InvalidInputException {
		try {
			ResponseEntity<ApiResponse<ExchangeRateResponseModel>> response = null;
			String endpoint = EXCHANGE_RATE_ENDPOINT + "/online/";
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("fromCurrency=").append(fromCurrency);
			sb.append("&").append("toCurrency=").append(toCurrency);
			sb.append("&").append("amount=").append(amount);
			if (bankId != null) {
				sb.append("&").append("bankId=").append(bankId);
			}
			String getExchangeRateUrl = this.getBaseUrl() + endpoint + sb.toString();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			LOGGER.info("calling getExchangeRate api: " + getExchangeRateUrl);
			response = restTemplate.exchange(getExchangeRateUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ExchangeRateResponseModel>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getExchangeRate : ",e);
            throw new JaxSystemError();
        } // end of try-catch

	}

	public ApiResponse<BooleanResponse> setExchangeRate(String toCurrency, BigDecimal amount)
			throws ResourceNotFoundException, InvalidInputException {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response = null;
			String endpoint = EXCHANGE_RATE_ENDPOINT + "/online/";
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("quoteName=").append(toCurrency);
			sb.append("&").append("value=").append(amount);

			String getExchangeRateUrl = this.getBaseUrl() + endpoint + sb.toString();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			LOGGER.info("calling getExchangeRate api: " + getExchangeRateUrl);
			response = restTemplate.exchange(getExchangeRateUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in setExchangeRate : ",e);
            throw new JaxSystemError();
        } // end of try-catch

	}
}
