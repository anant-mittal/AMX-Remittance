package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.repository.HolidayListRepository;

@Component
public class HolidayListDao {

	@Autowired
	HolidayListRepository holidayListRepository;

	public List<HolidayListMasterModel> getHolidayListForDateRange(BigDecimal countryId, Date fromDate, Date toDate) {
		List<HolidayListMasterModel> holidayList = holidayListRepository.getHolidayList(countryId, fromDate, toDate);

		return holidayList;
	}

}
