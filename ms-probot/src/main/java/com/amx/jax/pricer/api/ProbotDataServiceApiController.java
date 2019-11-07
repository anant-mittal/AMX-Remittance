package com.amx.jax.pricer.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.pricer.ProbotDataService;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.RateUploadRuleDto;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.service.ExchangeDataService;
import com.amx.jax.pricer.service.HolidayListService;
import com.amx.utils.JsonUtil;

@RestController
public class ProbotDataServiceApiController implements ProbotDataService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProbotDataServiceApiController.class);

	@Autowired
	HolidayListService holidayService;

	@Autowired
	ExchangeDataService dataService;

	@Override
	@RequestMapping(value = ApiEndPoints.GET_DISCOUNT_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemet(
			@RequestBody @Valid DiscountMgmtReqDTO discountMgmtReqDTO) {
		LOGGER.info("Received Request for Discount Management " + " with TraceId: " + AppContextUtil.getTraceId());
		DiscountDetailsReqRespDTO discountMgmtRespDTO = dataService.getDiscountManagementData(discountMgmtReqDTO);

		return AmxApiResponse.build(discountMgmtRespDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_ROUTBANKS_AND_SEVICES, method = RequestMethod.POST)
	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(
			@RequestParam(required = true) BigDecimal countryId, @RequestParam(required = true) BigDecimal currencyId) {
		LOGGER.info("Get Country Id And Currency Id For Routing Bank and Service " + " with TraceId: "
				+ AppContextUtil.getTraceId());
		List<RoutBanksAndServiceRespDTO> routBanksAndServiceRespDTO = dataService.getRoutBanksAndServices(countryId,
				currencyId);

		return AmxApiResponse.buildList(routBanksAndServiceRespDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_HOLIDAYS_DATE_RANGE, method = RequestMethod.POST)
	public AmxApiResponse<HolidayResponseDTO, Object> fetchHolidayList(
			@RequestParam(required = true) BigDecimal countryId,
			@RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

		LOGGER.info("Received Holiday List Request " + " with TraceId: " + AppContextUtil.getTraceId());

		List<HolidayResponseDTO> holidayResponseDTO = holidayService.getHolidayList(countryId, fromDate, toDate);

		return AmxApiResponse.buildList(holidayResponseDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.SAVE_DISCOUNT_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(
			@RequestBody DiscountDetailsReqRespDTO discountdetailsRequestDTO) {

		// TODO : Log proper Info : Subodh
		LOGGER.info("Recieved Save Request for Discount Details " + " with TraceId: " + AppContextUtil.getTraceId());
		return dataService.saveDiscountDetails(discountdetailsRequestDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_CUR_GROUPING_DATA, method = RequestMethod.POST)
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData() {

		LOGGER.info("Get Currency Grouping Data " + " with TraceId: " + AppContextUtil.getTraceId());
		List<GroupDetails> groupInfoForCurrency = dataService.getGroupInfoForCurrency();

		return AmxApiResponse.buildList(groupInfoForCurrency);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.UPDATE_CUR_GROUP_ID, method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(
			@RequestParam(required = true) BigDecimal groupId, @RequestParam(required = true) BigDecimal currencyId) {
		LOGGER.info("Get Group Id And Currency Id For Update " + " with TraceId: " + AppContextUtil.getTraceId());
		return dataService.updateCurrencyGroupId(groupId, currencyId);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_CUR_BY_GROUP_ID, method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(
			@RequestParam(required = true) BigDecimal groupId) {

		LOGGER.info("Get Group Id For Currency " + " with TraceId: " + AppContextUtil.getTraceId());
		List<CurrencyMasterDTO> groupInfoForCurrency = dataService.getCurrencyByGroupId(groupId);

		return AmxApiResponse.buildList(groupInfoForCurrency);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_MARKUP_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData(
			@RequestBody @Valid OnlineMarginMarkupReq onlineMarginMarkupReq) {
		LOGGER.info("Received Request for markup " + onlineMarginMarkupReq.toString());
		OnlineMarginMarkupInfo marginMarkupResp = dataService.getOnlineMarginMarkupData(onlineMarginMarkupReq);
		return AmxApiResponse.build(marginMarkupResp);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.SAVE_MARKUP_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData(
			@RequestBody @Valid OnlineMarginMarkupInfo onlineMarginMarkupInfo) {
		LOGGER.info("Received Request for save  markup " + onlineMarginMarkupInfo.toString());
		BoolRespModel marginMarkupResp = dataService.saveOnlineMarginMarkupData(onlineMarginMarkupInfo);
		;
		return AmxApiResponse.build(marginMarkupResp);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_GROUPS_OF_TYPE, method = RequestMethod.POST)
	public AmxApiResponse<GroupDetails, Object> getGroupsOfType(@RequestParam(required = true) String groupType) {

		LOGGER.info("Received Probot API Service Request for getting groups of type: " + groupType);

		List<GroupDetails> groups = dataService.getGroupsOfType(groupType);

		return AmxApiResponse.buildList(groups);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.SAVE_GROUPS, method = RequestMethod.POST)
	public AmxApiResponse<GroupDetails, Object> saveGroups(@RequestBody @Valid GroupDetails group) {
		LOGGER.info("Received Probot API Service Request for Save groups: " + JsonUtil.toJson(group));

		GroupDetails details = dataService.saveGroup(group);

		return AmxApiResponse.build(details);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.ENQUIRE_EXCH_RATE, method = RequestMethod.POST)
	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(
			@RequestBody @Valid ExchRateEnquiryReqDto rateEnquiryReqDto) {

		LOGGER.info("Received Probot API Service Request for Exchange Rate Enquiry");

		ExchangeRateEnquiryRespDto respDto = dataService.enquireExchRate(rateEnquiryReqDto);

		return AmxApiResponse.build(respDto);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.RATE_UPLOAD_RULE_MAKER, method = RequestMethod.POST)
	public AmxApiResponse<Long, Object> rateUpoadRuleMaker(
			@RequestBody @Valid ArrayList<RateUploadRuleDto> rateUploadRules) {

		LOGGER.info("Received Probot API Service Request for Rate Upload Rule Maker");

		Long rowsUpdated = dataService.rateUpoadRuleMaker(rateUploadRules);

		return AmxApiResponse.build(rowsUpdated);
	}

}
