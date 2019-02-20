package com.amx.jax.pricer;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.pricer.dto.DprRequestDto;
import com.amx.jax.pricer.dto.HolidayRequestDTO;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.rest.RestService;

@Component
public class PricerServiceClient implements ProbotExchangeRateService{

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

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_PRICE_CUSTOMER)
				.post(pricingRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<PricingResponseDTO, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Rate/Price Check Request Called for : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_BASE_PRICE).post(pricingRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<PricingResponseDTO, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO) {
		LOGGER.info("Get Discounted Rate/Price Request Called for : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_DISCOUNTED_RATES)
				.post(pricingRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<PricingResponseDTO, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<PricingResponseDTO, Object> fetchRemitRoutesAndPrices(DprRequestDto dprRequestDTO) {

		LOGGER.info("Get Remit Routes for Rate/Price Request Called for : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_REMIT_ROUTES_PRICES).post(dprRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<PricingResponseDTO, Object>>() {
				});
	}
	
	public AmxApiResponse<HolidayResponseDTO, Object> fetchHolidayList(HolidayRequestDTO holidayRequestDTO) {

		LOGGER.info("Get Holiday List for Given CountryId and Event Date");

		 return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.HOLIDAY_LIST).post(holidayRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<HolidayResponseDTO, Object>>() {
				});
	}

}
