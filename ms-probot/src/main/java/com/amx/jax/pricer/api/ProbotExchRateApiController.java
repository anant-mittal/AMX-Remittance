package com.amx.jax.pricer.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.ProbotExchangeRateService;
import com.amx.jax.pricer.dto.DprRequestDto;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.service.HolidayListService;
import com.amx.jax.pricer.service.PricingService;
import com.amx.jax.pricer.util.ExchangeRateRequestDataCache;
import com.amx.jax.pricer.var.PricerServiceConstants;

@RestController
public class ProbotExchRateApiController implements ProbotExchangeRateService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProbotExchRateApiController.class);

	@Resource
	PricingService pricingService;
	
	@Autowired
	HolidayListService holidayService;

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ExchangeRateRequestDataCache ExchangeRateRequestDataCache() {
		ExchangeRateRequestDataCache exchangeRateRequestDataCache = new ExchangeRateRequestDataCache();
		return exchangeRateRequestDataCache;
	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_PRICE_CUSTOMER, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(
			@RequestBody @Valid PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Received Pricing Request from customer Id : " + pricingRequestDTO.getCustomerId()
				+ " with TraceId: " + AppContextUtil.getTraceId());

		StopWatch watch = new StopWatch();
		watch.start();

		PricingResponseDTO pricingResponseDTO = pricingService.fetchRemitPricesForCustomer(pricingRequestDTO);

		watch.stop();

		if (null == pricingResponseDTO.getInfo()) {
			pricingResponseDTO.setInfo(new HashMap<>());
		}
		pricingResponseDTO.getInfo().put(PricerServiceConstants.TTE, watch.getLastTaskTimeMillis());

		return AmxApiResponse.build(pricingResponseDTO);

	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_BASE_PRICE, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(
			@RequestBody @Valid PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Received Base Rate Request " + " with TraceId: " + AppContextUtil.getTraceId());

		PricingResponseDTO pricingResponseDTO = pricingService.fetchBaseRemitPrices(pricingRequestDTO);

		return AmxApiResponse.build(pricingResponseDTO);

	}

	/*@Override
	@RequestMapping(value = ApiEndPoints.FETCH_DISCOUNTED_RATES, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchCustomercategoryDiscountedRates(
			PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Received Discounted Rate Request " + " with TraceId: " + AppContextUtil.getTraceId());
		List<PricingResponseDTO> pricingResponseDTOList = pricingService
				.fetchDiscountedRatesAcrossCustCategories(pricingRequestDTO);

		return AmxApiResponse.buildList(pricingResponseDTOList);
	}*/
	
	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_DISCOUNTED_RATES, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(
			@RequestBody @Valid PricingRequestDTO pricingRequestDTO) {

		LOGGER.info("Received Fetch Discounted Rate Request " + " with TraceId: " + AppContextUtil.getTraceId());

		LOGGER.info("Received Discounted Rate Request " + " with TraceId: " + AppContextUtil.getTraceId());
		List<PricingResponseDTO> pricingResponseDTOList = pricingService
				.fetchDiscountedRatesAcrossCustCategories(pricingRequestDTO);

		return AmxApiResponse.buildList(pricingResponseDTOList);

	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_REMIT_ROUTES_PRICES, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchRemitRoutesAndPrices(
			@RequestBody @Valid DprRequestDto dprRequestDTO) {

		LOGGER.info("Received Fetch Remit Routes and Prices Request for Customer and beneficiary" + " with TraceId: "
				+ AppContextUtil.getTraceId());

		PricingResponseDTO pricingResponseDTO = pricingService.fetchRemitRoutesAndPrices(dprRequestDTO);

		return AmxApiResponse.build(pricingResponseDTO);

	}
	
	@RequestMapping(value = ApiEndPoints.HOLIDAY_LIST, method = RequestMethod.POST)
	public List<HolidayResponseDTO> fetchHolidayList(
			@RequestParam(required = true, value = "Id") BigDecimal Id,
			@RequestParam(required = true, value = "fromDate") String fromDate,
			@RequestParam(required = true, value = "toDate") String toDate)
			{
			

		LOGGER.info("Received Holiday List Request " + " with TraceId: "
		+ AppContextUtil.getTraceId());

		List<HolidayResponseDTO> holidayResponseDTO = holidayService.getHolidayList(Id,fromDate,toDate);
		

		return holidayResponseDTO;

	}

}
