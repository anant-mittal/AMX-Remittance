package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;

@Transactional
public interface HolidayListRepository extends CrudRepository<HolidayListMasterModel, BigDecimal> {
	
	
	@Query("select c from HolidayListMasterModel c where c.countryId=?1 and c.eventDate between to_date(?2,'dd-mm-yy') and to_date(?3,'dd-mm-yy')")
	public List<HolidayListMasterModel> getHolidayList(BigDecimal countryId,String fromDate , String toDate);
	
	
	
	
	
}
