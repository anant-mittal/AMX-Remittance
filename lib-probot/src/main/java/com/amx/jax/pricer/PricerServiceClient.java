package com.amx.jax.pricer;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.rest.RestService;
import com.amx.utils.JsonUtil;

@Component
public class PricerServiceClient implements ProbotExchangeRateService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(PricerServiceClient.class);

	/** The rest service. */
	@Autowired
	RestService restService;

	/** The app config. */
	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Pricing Request Called for : Customer Id: {}, transaction Id: {}, with TraceId: {}",
				pricingRequestDTO.getCustomerId(), AppContextUtil.getTranxId(), AppContextUtil.getTraceId());
		LOGGER.info("Pricing Request fetchPriceForCustomer: URL: {} , Request Body: {}", appConfig.getPricerURL(),
				JsonUtil.toJson(pricingRequestDTO));
		AmxApiResponse<PricingResponseDTO, Object> response = restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_PRICE_CUSTOMER)
				.post(pricingRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<PricingResponseDTO, Object>>() {
				});
		LOGGER.info("Pricing Request fetchPriceForCustomer: Responce Body: {}", JsonUtil.toJson(response.getResult()));
		return response;
	}

	@Override
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Rate/Price Check Request Called for : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());
		LOGGER.info("Pricing Request fetchBasePrice: URL: {} , Request Body: {}", appConfig.getPricerURL(),
				JsonUtil.toJson(pricingRequestDTO));
		AmxApiResponse<PricingResponseDTO, Object> response = restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_BASE_PRICE).post(pricingRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<PricingResponseDTO, Object>>() {
				});
		LOGGER.info("Pricing Request fetchBasePrice: Responce Body: {}", JsonUtil.toJson(response.getResult()));
		return response;
	}

}
