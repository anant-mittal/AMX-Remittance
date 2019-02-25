package com.amx.jax.pricer.api;

import java.math.BigDecimal;
import com.amx.jax.AppContextUtil;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.ProbotDataService;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.service.HolidayListService;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountMgmtRespDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.service.DataService;

@RestController
public class ProbotDataServiceApiController implements ProbotDataService{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProbotDataServiceApiController.class);
	
	@Autowired
	HolidayListService holidayService;

	@Resource
	DataService dataService;

	@Override
	@RequestMapping(value = ApiEndPoints.GET_DISCOUNT_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<DiscountMgmtRespDTO, Object> getDiscountManagemet(
			@RequestBody @Valid DiscountMgmtReqDTO discountMgmtReqDTO) {
		LOGGER.info("In Get API of Discount Management");
		
		DiscountMgmtRespDTO discountMgmtRespDTO = dataService.getDiscountManagementData(discountMgmtReqDTO);
		
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
