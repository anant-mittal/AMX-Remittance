package com.amx.jax.pricer;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.rest.RestService;

@Component
public class PricerServiceClient implements ProbotExchangeRateService, ProbotDataService {

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
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemet(
			DiscountMgmtReqDTO discountMgmtReqDTO) {
		LOGGER.info("Get Discounted Mgmt Amount Slab : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_DISCOUNT_DETAILS)
				.post(discountMgmtReqDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<DiscountDetailsReqRespDTO, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(BigDecimal countryId,
			BigDecimal currencyId) {
		LOGGER.info("Get Routing Banks and Services : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_ROUTBANKS_AND_SEVICES)
				.queryParam("countryId", countryId).queryParam("currencyId", currencyId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<RoutBanksAndServiceRespDTO, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<ExchangeRateAndRoutingResponse, Object> fetchRemitRoutesAndPrices(
			ExchangeRateAndRoutingRequest dprRequestDTO) {

		LOGGER.info("Get Remit Routes for Rate/Price Request Called for : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_REMIT_ROUTES_PRICES)
				.post(dprRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<ExchangeRateAndRoutingResponse, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<HolidayResponseDTO, Object> fetchHolidayList(BigDecimal countryId, Date fromDate,
			Date toDate) {

		LOGGER.info("Get Holiday List for Given CountryId and Event Date");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_HOLIDAYS_DATE_RANGE)
				.queryParam("countryId", countryId).queryParam("fromDate", dateFormat.format(fromDate))
				.queryParam("toDate", dateFormat.format(toDate)).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<HolidayResponseDTO, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(
			DiscountDetailsReqRespDTO discountMgmtReqDTO) {
		LOGGER.info("Save Discount Management Details : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.SAVE_DISCOUNT_DETAILS)
				.post(discountMgmtReqDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<DiscountDetailsReqRespDTO, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData() {
		LOGGER.info("Get Currency Group Data : transaction Id: {}, with TraceId: {}", AppContextUtil.getTranxId(),
				AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_CUR_GROUPING_DATA).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<GroupDetails, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(BigDecimal groupId, BigDecimal currencyId) {
		LOGGER.info("Update Currency Group Id : transaction Id: {}, with TraceId: {}", AppContextUtil.getTranxId(),
				AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.UPDATE_CUR_GROUP_ID)
				.queryParam("groupId", groupId).queryParam("currencyId", currencyId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(BigDecimal groupId) {
		LOGGER.info("Get Currency By Group Id : transaction Id: {}, with TraceId: {}", AppContextUtil.getTranxId(),
				AppContextUtil.getTraceId());
		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_CUR_BY_GROUP_ID)
				.queryParam("groupId", groupId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
				});
	}

}
