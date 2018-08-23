package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.rbaac.dbmodel.PermissionMaster;

public interface OldIPermissionRepository extends JpaRepository<PermissionMaster, Serializable>{
	
	@Query(value = "select * from EX_PERMISSION_MASTER where MODULE_ID=?1 and FUNCTIONALITY_TYPE_ID=?2 and FUNCTIONALITY=?3 and ISACTIVE='Y'", nativeQuery = true)
	public PermissionMaster getPermissionMaster(BigDecimal moduleId,BigDecimal functionalityTypeId,String functionality);
	
}

