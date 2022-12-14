package com.amx.jax.pricer;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.PricingAndCostResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.RateUploadRequestDto;
import com.amx.jax.pricer.dto.RateUploadRuleDto;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.dto.RoutingProductStatusDetails;
import com.amx.jax.pricer.dto.RoutingStatusUpdateRequestDto;
import com.amx.jax.pricer.dto.RoutingCountryBankInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.GROUP_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;
import com.amx.jax.rest.RestService;

@Component
public class PricerServiceClient implements ProbotExchangeRateService, ProbotDataService, PartnerDataService {

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
	public AmxApiResponse<PricingAndCostResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO) {
		LOGGER.info("Get Discounted Rate/Price Request Called for : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.FETCH_DISCOUNTED_RATES)
				.post(pricingRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<PricingAndCostResponseDTO, Object>>() {
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

	@Override
	public AmxApiResponse<SrvPrvFeeInqResDTO, Object> getServiceProviderQuotation(
			SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {

		LOGGER.info("fetch Service Provider Exchange Details : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_SERVICE_PROVIDER_QUOTE)
				.post(srvPrvFeeInqReqDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<SrvPrvFeeInqResDTO, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData(
			OnlineMarginMarkupReq OnlineMarginMarkupReq) {
		LOGGER.info("fetch Online Margin Markup Details : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_MARKUP_DETAILS)
				.post(OnlineMarginMarkupReq)
				.as(new ParameterizedTypeReference<AmxApiResponse<OnlineMarginMarkupInfo, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData(
			OnlineMarginMarkupInfo onlineMarginMarkupInfo) {

		LOGGER.info("fetch Online Margin Markup Details : transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());
		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.SAVE_MARKUP_DETAILS)
				.post(onlineMarginMarkupInfo)
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<RoutingProductStatusDetails, Object> getRoutingProductStatus(BigDecimal countryId,
			BigDecimal currencyId) {
		LOGGER.info("Get Routing Product Status Client with transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());
		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_ROUTING_PRODUCT_STATUS)
				.queryParam("countryId", countryId).queryParam("currencyId", currencyId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<RoutingProductStatusDetails, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<Integer, Object> updateRoutingProductStatus(RoutingStatusUpdateRequestDto request) {

		LOGGER.info("Update Routing Product Status Client with transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.UPDATE_ROUTING_STATUS).post(request)
				.as(new ParameterizedTypeReference<AmxApiResponse<Integer, Object>>() {
				});
	}
	
	@Override
	public AmxApiResponse<GroupDetails, Object> getGroupsOfType(GROUP_TYPE groupType) {
		LOGGER.info("Get Group Data for Type: {}, transaction Id: {}, with TraceId: {}", groupType,
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_GROUPS_OF_TYPE)
				.queryParam("groupType", groupType).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<GroupDetails, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<GroupDetails, Object> saveGroup(GroupDetails group) {
		LOGGER.info("Save Group Data transaction Id: {}, with TraceId: {}", AppContextUtil.getTranxId(),
				AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.SAVE_GROUP).post(group)
				.as(new ParameterizedTypeReference<AmxApiResponse<GroupDetails, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<Long, Object> deleteGroup(BigDecimal applicationCountryId, BigDecimal groupId,
			GROUP_TYPE groupType, String groupName) {

		LOGGER.info("Delete Group Data transaction Id: {}, with TraceId: {}", AppContextUtil.getTranxId(),
				AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.DELETE_GROUP)
				.queryParam("applicationCountryId", applicationCountryId).queryParam("groupId", groupId)
				.queryParam("groupType", groupType).queryParam("groupName", groupName).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<Long, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(
			ExchRateEnquiryReqDto rateEnquiryReqDto) {
		LOGGER.info("Received Client Request for Enquire Exchange Rates, transaction Id: {}, with TraceId: {}",
				AppContextUtil.getTranxId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.ENQUIRE_EXCH_RATE).post(rateEnquiryReqDto)
				.as(new ParameterizedTypeReference<AmxApiResponse<ExchangeRateEnquiryRespDto, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<Long, Object> rateUpoadRuleMaker(RateUploadRequestDto rateUploadRequestDto) {
		LOGGER.info("Received Probot API Service Request for Rate Upload Rule Maker Client");

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.RATE_UPLOAD_RULE_MAKER)
				.post(rateUploadRequestDto).as(new ParameterizedTypeReference<AmxApiResponse<Long, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<Long, Object> rateUpoadRuleChecker(RateUploadRequestDto rateUploadRequestDto) {
		LOGGER.info("Received Probot API Service Request for Rate Upload Rule Checker Client");

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.RATE_UPLOAD_RULE_CHECKER)
				.post(rateUploadRequestDto).as(new ParameterizedTypeReference<AmxApiResponse<Long, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<List<RateUploadRuleDto>, Object> getRateUploadRulesByStatus(RATE_UPLOAD_STATUS status,
			Boolean onlyActive) {
		LOGGER.info("Received Probot API Service Request for Get Rate Upload Rule By Status Client");
		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_RATE_UPLOAD_RULES)
				.queryParam("status", status).queryParam("onlyActive", onlyActive).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<List<RateUploadRuleDto>, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<RoutingCountryBankInfo, Object> getRoutingCountryBanksForCurrency(BigDecimal currencyId) {

		LOGGER.info("Received Probot API Service Request for Get Routing Country Banks for Currency Client");

		return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.GET_ROUTE_COUNTRY_BANKS)
				.queryParam("currencyId", currencyId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<RoutingCountryBankInfo, Object>>() {
				});
	}

}
