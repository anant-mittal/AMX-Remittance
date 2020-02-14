package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.def.CacheForTenant;
import com.amx.jax.pricer.dao.HolidayListDao;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.dto.HolidayResponseDTO;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class HolidayListManager {

	@Autowired
	HolidayListDao holidayListDao;

	//@CacheForTenant
	public List<HolidayResponseDTO> getHolidayList(BigDecimal countryId, Date fromDate, Date toDate) {

		List<HolidayResponseDTO> holidayResponseDTO = new ArrayList<HolidayResponseDTO>();

		List<HolidayListMasterModel> holidaylist = getHoidaysForCountryAndDateRange(countryId, fromDate, toDate);

		if (!holidaylist.isEmpty()) {
			for (HolidayListMasterModel rec : holidaylist) {
				HolidayResponseDTO holidayDTO = new HolidayResponseDTO();

				holidayDTO.setAllDayEvent(rec.getAllDayEvent());
				holidayDTO.setCountryId(rec.getCountryId());
				holidayDTO.setEndTime(rec.getEndTime());
				holidayDTO.setEventCategory(rec.getEventCategory());
				holidayDTO.setEventDate(rec.getEventDate());
				holidayDTO.setEventDayOfTheWeek(rec.getEventDayOfTheWeek());
				holidayDTO.setEventName(rec.getEventName());
				holidayDTO.setEventYear(rec.getEventYear());
				holidayDTO.setStartTime(rec.getStartTime());

				holidayResponseDTO.add(holidayDTO);
			}
		}

		return holidayResponseDTO;
	}

	/**
	 * 
	 * @param countryId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@CacheForTenant
	public List<HolidayListMasterModel> getHoidaysForCountryAndDateRange(BigDecimal countryId, Date fromDate,
			Date toDate) {
		List<HolidayListMasterModel> holidaylist = holidayListDao.getHolidayListForDateRange(countryId, fromDate,
				toDate);

		if (null == holidaylist) {
			holidaylist = new ArrayList<HolidayListMasterModel>();
		}

		Collections.sort(holidaylist);

		return holidaylist;
	}

}
