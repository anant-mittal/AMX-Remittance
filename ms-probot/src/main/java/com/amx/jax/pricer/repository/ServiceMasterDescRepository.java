package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;

public interface ServiceMasterDescRepository extends CrudRepository<ServiceMasterDesc, BigDecimal> {

	@Query(value = "select * from EX_SERVICE_MASTER_DESC where SERVICE_ID=?1 and LANGUAGE_ID = 1 ",nativeQuery = true)
	ServiceMasterDesc getServiceById(BigDecimal serviceId);

}
