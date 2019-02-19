package com.amx.jax.pricer.api;

import java.util.List;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppContextUtil;
import com.amx.jax.pricer.AbstractProbotInterface.ApiEndPoints;
import com.amx.jax.pricer.service.HolidayListService;
import com.amx.jax.pricer.util.HolidayListDto;

@RestController
public class HolidayListServiceController {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayListServiceController.class);

	
	
	@Autowired
	HolidayListService holidayService;
	
	
	@RequestMapping(value = ApiEndPoints.HOLIDAY_LIST, method = RequestMethod.POST)
	public List<HolidayListDto> fetchHolidayList(
			@RequestBody @Valid HolidayListDto holidayListDTO) {

		LOGGER.info("Received Holiday List Request " + " with TraceId: " + AppContextUtil.getTraceId());

		List<HolidayListDto> pricingResponseDTOList = holidayService.getHolidayList(holidayListDTO.getCountryId(), holidayListDTO.getEventYear());
		
		return pricingResponseDTOList;
				
	}
	
	

}
