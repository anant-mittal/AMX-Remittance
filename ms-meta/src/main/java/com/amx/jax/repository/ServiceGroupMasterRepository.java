package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.meta.ServiceGroupMaster;

public interface ServiceGroupMasterRepository extends CrudRepository<ServiceGroupMaster, BigDecimal> {

	List<ServiceGroupMaster> findByServiceGroupIdAndIsActive(BigDecimal serviceGroupId, String isActive);

	List<ServiceGroupMaster> findByServiceGroupCodeAndIsActive(String serviceGroupCode, String isActive);
}
