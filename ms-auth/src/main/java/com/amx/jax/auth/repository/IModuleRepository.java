package com.amx.jax.auth.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.auth.dbmodel.ModuleMaster;

public interface IModuleRepository extends JpaRepository<ModuleMaster, Serializable>{
	
	public ModuleMaster findByModuleEnum(String moduleName);
	
}
