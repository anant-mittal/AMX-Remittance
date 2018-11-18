package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.FxDeliveryTimeSlotMaster;

public interface FxOrderDeliveryTimeSlotRepository extends CrudRepository<FxDeliveryTimeSlotMaster, Serializable>{
	
	public List<FxDeliveryTimeSlotMaster> findByCountryIdAndCompanyIdAndIsActive(BigDecimal countryId,BigDecimal companyId,String isactive);
}
