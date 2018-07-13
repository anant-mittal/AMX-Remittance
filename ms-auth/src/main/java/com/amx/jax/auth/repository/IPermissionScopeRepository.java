package com.amx.jax.auth.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.auth.dbmodel.PermissionScopeMaster;

public interface IPermissionScopeRepository extends JpaRepository<PermissionScopeMaster, Serializable>{
	
	public PermissionScopeMaster findByScopeEnum(String permScope);
	
}
