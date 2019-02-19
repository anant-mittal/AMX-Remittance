package com.amx.jax.pricer.service;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.pricer.dao.HolidayListDao;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.util.HolidayListDto;

@Service
public class HolidayListService {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayListService.class);
	
	
	
	HolidayListDto holidayListDTO;
	
	@Autowired
	HolidayListDao holidayListDao;

	
	
public List<HolidayListDto> getHolidayList(BigDecimal countryId, BigDecimal eventYear) {
	
		
		validateHolidayRequest(holidayListDTO, Boolean.FALSE);
		
		List<HolidayListDto> holidaylist = holidayListDao.getHolidayListForDateRange(holidayListDTO.getCountryId(), holidayListDTO.getEventYear());
		
		if (null == holidaylist) {

			LOGGER.info("Invalid Country Id : " + holidayListDTO.getCountryId());
			LOGGER.info("Invalid Event Year : " + holidayListDTO.getEventYear());
			

			throw new PricerServiceException(PricerServiceError.INVALID_COUNTRY_ID,
					"Invalid Country Id :  " + holidayListDTO.getCountryId()
					/*+"/"+PricerServiceError.INVALID_EVENT_YEAR,"Invalid Event Year"*/);
			

		}
		
		////Collections.sort((List<T>) holidayListDTO.getEventDate(), Collections.reverseOrder());
	
		return holidaylist;
				
	}

		
		
		
	

private boolean validateHolidayRequest(HolidayListDto holidayListDTO, boolean isCountryId) {
	
	if(isCountryId && null == holidayListDTO.getCountryId()) {
		
		throw new PricerServiceException(PricerServiceError.MISSING_COUNTRY_ID,
				"Country Id can not be blank or empty");
		
	}
	
	if(holidayListDTO.getEventYear() == null) {
		throw new PricerServiceException(PricerServiceError.MISSING_EVENT_YEAR,
				"Event Year can not be blank or empty");
		
	}
	
	return Boolean.TRUE;
	
}

	
	

}
