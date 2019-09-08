package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.amx.jax.dbmodel.fx.FxDeliveryTimeSlotMaster;

public interface FxOrderDeliveryTimeSlotRepository extends CrudRepository<FxDeliveryTimeSlotMaster, Serializable>{
	
	public List<FxDeliveryTimeSlotMaster> findByCountryIdAndCompanyIdAndIsActive(BigDecimal countryId,BigDecimal companyId,String isactive);
	

	@Query(value = "SELECT * FROM EX_DELIVERY_TIME_SLOTS WHERE COUNTRY_ID=?1 AND COMPANY_ID=?2 AND ISACTIVE=?3", nativeQuery = true)
	public FxDeliveryTimeSlotMaster saveDeliveryTimeSlot(BigDecimal countryId,BigDecimal companyId,String isactive);
		
	//@Transactional
	//@Modifying
	//@Query("update FxDeliveryTimeSlotMaster fxDelv set fxDelv.startTime =:startTime,fxDelv.endTime =:endTime, fxDelv.timeInterval=:timeInterval where fxDelv.countryId=:countryId,fxDelv.companyId=:companyId")
	//public void updatestartTimeendTimetimeInterval(@Param("startTime") BigDecimal startTime,@Param("endTime") BigDecimal endTime,@Param("timeInterval") BigDecimal timeInterval,@Param("countryId") BigDecimal countryId,@Param("companyId") BigDecimal companyId);

	//@Transactional
	//@Modifying
	//@Query("update FxDeliveryTimeSlotMaster fxDelv set fxDelv.officeStartTime =:officeStartTime,fxDelv.officeEndTime =:officeEndTime where fxDelv.timeInterval=:timeInterval where fxDelv.countryId=:countryId,fxDelv.companyId=:companyId")
	//public void updatestartTimeofficeEndTimetimeInterval(@Param("officeStartTime") BigDecimal startTime,@Param("officeEndTime") BigDecimal endTime,@Param("timeInterval") BigDecimal timeInterval,@Param("countryId") BigDecimal countryId,@Param("companyId") BigDecimal companyId);

	
	
}
