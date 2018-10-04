package com.amx.jax.rbaac.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.FunctionalityTypeMaster;

public interface OldIFunctionalityTypeRepository extends JpaRepository<FunctionalityTypeMaster, Serializable>{
	
	public FunctionalityTypeMaster findByFunctionalityTypeEnum(String functionalityTypeDescription);
	
}
