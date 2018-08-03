package com.amx.jax.auth.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.auth.dbmodel.FunctionalityTypeMaster;

public interface IFunctionalityTypeRepository extends JpaRepository<FunctionalityTypeMaster, Serializable>{
	
	public FunctionalityTypeMaster findByFunctionalityTypeEnum(String functionalityTypeDescription);
	
}
