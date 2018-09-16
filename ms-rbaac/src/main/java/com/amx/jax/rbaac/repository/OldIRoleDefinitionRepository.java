package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.RoleDefinition;

public interface OldIRoleDefinitionRepository extends JpaRepository<RoleDefinition, Serializable>{

	public List<RoleDefinition> findByRoleId(BigDecimal roleId);
	
	public RoleDefinition findByRoleDefId(BigDecimal roleDefId);
	
	public RoleDefinition findByRoleIdAndPermissionIdAndScopeId(BigDecimal roleId,BigDecimal permissionId,BigDecimal scopeId);

}
