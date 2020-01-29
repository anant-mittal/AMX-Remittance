package com.amx.jax.pricer.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.manager.HolidayListManager;

@Service
public class HolidayListService {

	@Autowired
	HolidayListManager holidayListManager;

	public List<HolidayResponseDTO> getHolidayList(BigDecimal countryId, Date fromDate, Date toDate) {
		
		return holidayListManager.getHolidayList(countryId, fromDate, toDate);
	}

}
