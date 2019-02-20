package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;

@Transactional
public interface HolidayListRepository extends CrudRepository<HolidayListMasterModel, BigDecimal> {
	
	@Query("select c from HolidayListMasterModel c where c.countryId=? and c.eventDate >= (trunc(sysdate))- 51 and c.eventDate<=trunc(sysdate)")
	public List<HolidayListMasterModel> getHolidayList(BigDecimal countryId);

	
	
	
	
}
