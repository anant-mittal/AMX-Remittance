package com.amx.jax.pricer.api;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.cache.ExchRateAndRoutingTransientDataCache;
import com.amx.jax.pricer.ProbotExchangeRateService;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.service.ExchangePricingAndRoutingService;
import com.amx.jax.pricer.service.HolidayListService;

@RestController
public class ProbotExchRateApiController implements ProbotExchangeRateService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProbotExchRateApiController.class);

	@Autowired
	ExchangePricingAndRoutingService exchangePricingAndRoutingService;

	@Autowired
	HolidayListService holidayService;

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ExchRateAndRoutingTransientDataCache ExchRateAndRoutingTransientDataCache() {
		ExchRateAndRoutingTransientDataCache exchRateAndRoutingTransientDataCache = new ExchRateAndRoutingTransientDataCache();
		return exchRateAndRoutingTransientDataCache;
	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_PRICE_CUSTOMER, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(
			@RequestBody @Valid PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Received Pricing Request from customer Id : " + pricingRequestDTO.getCustomerId()
				+ " with TraceId: " + AppContextUtil.getTraceId());

		PricingResponseDTO pricingResponseDTO = exchangePricingAndRoutingService
				.fetchRemitPricesForCustomer(pricingRequestDTO);

		if (null == pricingResponseDTO.getInfo()) {
			pricingResponseDTO.setInfo(new HashMap<>());
		}

		return AmxApiResponse.build(pricingResponseDTO);

	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_BASE_PRICE, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(
			@RequestBody @Valid PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Received Base Rate Request " + " with TraceId: " + AppContextUtil.getTraceId());

		PricingResponseDTO pricingResponseDTO = exchangePricingAndRoutingService
				.fetchBaseRemitPrices(pricingRequestDTO);

		return AmxApiResponse.build(pricingResponseDTO);

	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_DISCOUNTED_RATES, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(
			@RequestBody @Valid PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Received Fetch Discounted Rate Request " + " with TraceId: " + AppContextUtil.getTraceId());

		List<PricingResponseDTO> pricingResponseDTOList = exchangePricingAndRoutingService
				.fetchDiscountedRatesAcrossCustCategories(pricingRequestDTO);

		return AmxApiResponse.buildList(pricingResponseDTOList);

	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_REMIT_ROUTES_PRICES, method = RequestMethod.POST)
	public AmxApiResponse<ExchangeRateAndRoutingResponse, Object> fetchRemitRoutesAndPrices(
			@RequestBody @Valid ExchangeRateAndRoutingRequest dprRequestDTO) {

		LOGGER.info("Received Fetch Remit Routes and Prices Request for Customer and beneficiary" + " with TraceId: "
				+ AppContextUtil.getTraceId());

		ExchangeRateAndRoutingResponse exchangeRateAndRoutingResponse = exchangePricingAndRoutingService
				.fetchRemitRoutesAndPrices(dprRequestDTO);

		return AmxApiResponse.build(exchangeRateAndRoutingResponse);

	}
	

}
