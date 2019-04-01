package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.repository.HolidayListRepository;
import com.amx.utils.DateUtil;

@Component
public class HolidayListDao {

	@Autowired
	HolidayListRepository holidayListRepository;

	public List<HolidayListMasterModel> getHolidayListForDateRange(BigDecimal countryId, Date fromDate, Date toDate) {
		
		Date startOfFromDate = DateUtil.removeTime(fromDate);
		
		List<HolidayListMasterModel> holidayList = holidayListRepository.getHolidayList(countryId, startOfFromDate, toDate);
		return holidayList;
	}

}
