package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.DeclarationModel;

public interface DeclarationDao extends JpaRepository<DeclarationModel, Serializable> {
	
	@Query("select a from DeclarationModel a where a.languageId =?1")	
	public List<DeclarationModel> getDeclarsList(BigDecimal languageId);
	
	
	@Query("select a from DeclarationModel a where a.languageId =?1")	
	public DeclarationModel getDeclars(BigDecimal languageId);
}
