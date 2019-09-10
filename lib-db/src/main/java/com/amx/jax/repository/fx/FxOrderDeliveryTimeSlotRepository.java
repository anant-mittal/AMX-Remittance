package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.fx.FxDeliveryTimeSlotMaster;

public interface FxOrderDeliveryTimeSlotRepository extends CrudRepository<FxDeliveryTimeSlotMaster, Serializable>{
	
	public List<FxDeliveryTimeSlotMaster> findByCountryIdAndCompanyIdAndIsActive(BigDecimal countryId,BigDecimal companyId,String isactive);
	

	@Query(value = "SELECT * FROM EX_DELIVERY_TIME_SLOTS WHERE COUNTRY_ID=?1 AND COMPANY_ID=?2 AND ISACTIVE=?3", nativeQuery = true)
	public FxDeliveryTimeSlotMaster saveDeliveryTimeSlot(BigDecimal countryId,BigDecimal companyId,String isactive);
		
	
}
