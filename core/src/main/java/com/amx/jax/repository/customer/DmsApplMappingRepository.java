package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.DmsApplMapping;

public interface DmsApplMappingRepository extends CrudRepository<DmsApplMapping, Serializable> {

	@Query(value = "select * from EX_DMS_APPL_MAP where CUSTOMER_ID = ?1 and IDENTITY_INT= ?2 and IDENTITY_TYPE_ID=?3 and trunc(IDENTITY_EXPIRY_DATE) = trunc(?4) order by CREATION_DATE desc", nativeQuery = true)
	public List<DmsApplMapping> getDmsApplMapping(BigDecimal customerId, String identityInt, BigDecimal identityType,
			Date expiryDate);
	
	@Query(value = "select * from EX_DMS_APPL_MAP where CUSTOMER_ID = ?1 ", nativeQuery = true)
	public List<DmsApplMapping> getDmsApplMapping(BigDecimal customerId);
}
