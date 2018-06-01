package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.dbmodel.meta.ServiceMaster;

public interface ServiceMasterRepository extends CrudRepository<ServiceMaster, BigDecimal> {

	public List<ServiceMaster> findByServiceGroupIdAndIsActive(ServiceGroupMaster ServiceGroupMaster, String isActive);
}
