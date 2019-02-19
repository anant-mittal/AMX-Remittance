package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.repository.HolidayListRepository;
import com.amx.jax.pricer.util.HolidayListDto;

@Component
public class HolidayListDao {
	
	@Autowired
	HolidayListRepository holidayListRepository;
	
	public List<HolidayListDto> getHolidayListForDateRange(BigDecimal countryId, BigDecimal eventYear){
	List<HolidayListDto> holidayList = holidayListRepository.getHolidayListForDateRange(countryId, eventYear);
	return holidayList;

}
}
