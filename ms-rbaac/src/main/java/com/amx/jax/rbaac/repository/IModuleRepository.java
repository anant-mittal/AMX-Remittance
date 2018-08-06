package com.amx.jax.rbaac.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.ModuleMaster;

public interface IModuleRepository extends JpaRepository<ModuleMaster, Serializable>{
	
	public ModuleMaster findByModuleEnum(String moduleName);
	
}
