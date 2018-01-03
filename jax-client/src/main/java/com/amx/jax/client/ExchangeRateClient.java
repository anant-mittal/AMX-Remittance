package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.EXCHANGE_RATE_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;

@Component
public class ExchangeRateClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());

	public ApiResponse<ExchangeRateResponseModel> getExchangeRate(BigDecimal fromCurrency, BigDecimal toCurrency,
			BigDecimal amount, BigDecimal bankId) throws ResourceNotFoundException, InvalidInputException {
		ResponseEntity<ApiResponse<ExchangeRateResponseModel>> response = null;
		try {
			String endpoint = EXCHANGE_RATE_ENDPOINT + "/online/";
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("fromCurrency=").append(fromCurrency);
			sb.append("&").append("toCurrency=").append(toCurrency);
			sb.append("&").append("amount=").append(amount);
			if (bankId != null) {
				sb.append("&").append("bankId=").append(bankId);
			}
			String getExchangeRateUrl = baseUrl.toString() + endpoint + sb.toString();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			log.info("calling getExchangeRate api: " + getExchangeRateUrl);
			response = restTemplate.exchange(getExchangeRateUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<ExchangeRateResponseModel>>() {
					});
		} catch (Exception e) {
			log.error("exception in getExchangeRate ", e);
		}
		checkResourceNotFoundException(response.getBody());
		//checkInvalidInputErrors(response.getBody());
		return response.getBody();
	}
}
