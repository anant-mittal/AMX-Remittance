package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.meta.ServiceGroupMasterDesc;

public interface ServiceGroupMasterDescRepository extends CrudRepository<ServiceGroupMasterDesc, BigDecimal> {

	@Query("select sgmd from ServiceGroupMasterDesc sgmd where sgmd.serviceGroupMasterId.isActive = 'Y' and sgmd.languageId=?")
	List<ServiceGroupMasterDesc> findActiveByLanguageId(BigDecimal langId);

}
