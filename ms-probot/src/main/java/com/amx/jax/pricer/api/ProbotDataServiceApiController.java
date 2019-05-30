package com.amx.jax.pricer.api;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.ProbotDataService;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.service.ExchangeDataService;

@RestController
public class ProbotDataServiceApiController implements ProbotDataService{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProbotDataServiceApiController.class);
	
	@Resource
	ExchangeDataService dataService;

	@Override
	@RequestMapping(value = ApiEndPoints.GET_DISCOUNT_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemet(
			@RequestBody @Valid DiscountMgmtReqDTO discountMgmtReqDTO) {
		LOGGER.info("In Get API of Discount Management");
		DiscountDetailsReqRespDTO discountMgmtRespDTO = dataService.getDiscountManagementData(discountMgmtReqDTO);
		
		return AmxApiResponse.build(discountMgmtRespDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_ROUTBANKS_AND_SEVICES, method = RequestMethod.POST)
	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(
			@RequestParam(required = true) BigDecimal countryId, @RequestParam(required = true) BigDecimal currencyId) {
		LOGGER.info("In Get API of Routing Bank and Services");
		List<RoutBanksAndServiceRespDTO> routBanksAndServiceRespDTO = dataService.getRoutBanksAndServices(countryId, currencyId);
		
		return AmxApiResponse.buildList(routBanksAndServiceRespDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.SAVE_DISCOUNT_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(
			@RequestBody DiscountDetailsReqRespDTO discountdetailsRequestDTO) {
		LOGGER.info("In Save API of Discount Details");
				
		return dataService.saveDiscountDetails(discountdetailsRequestDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_CUR_GROUPING_DATA, method = RequestMethod.POST)
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData() {
		List<GroupDetails> groupInfoForCurrency = dataService.getGroupInfoForCurrency();
		
		return AmxApiResponse.buildList(groupInfoForCurrency);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.UPDATE_CUR_GROUP_ID, method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(
			@RequestParam(required = true) BigDecimal groupId, @RequestParam(required = true) BigDecimal currencyId) {
		
		return dataService.updateCurrencyGroupId(groupId, currencyId);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_CUR_BY_GROUP_ID, method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(@RequestParam(required = true) BigDecimal groupId) {
		List<CurrencyMasterDTO> groupInfoForCurrency = dataService.getCurrencyByGroupId(groupId);
		
		return AmxApiResponse.buildList(groupInfoForCurrency);
	}
}
