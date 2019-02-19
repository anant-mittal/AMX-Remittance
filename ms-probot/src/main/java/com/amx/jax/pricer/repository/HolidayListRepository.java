package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.util.HolidayListDto;



@Transactional
public interface HolidayListRepository extends CrudRepository<HolidayListMasterModel, BigDecimal> {
	
	@Query("select h from HolidayListMasterModel h where h.countryId=?1 and h.eventYear =?2")
	public List<HolidayListDto> getHolidayListForDateRange(BigDecimal countryId, BigDecimal eventYear);


}
