package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.TermsAndCondition;

public interface ITermsAndConditionRepository<T> extends JpaRepository<TermsAndCondition,BigDecimal>{
	
	@Query("Select t from TermsAndCondition t where languageId=?")
	public List<TermsAndCondition> getTermsAndCondition(BigDecimal languageId);
	
	@Query("Select t from TermsAndCondition t where languageId=:languageId and countryId=:countryId")
	public List<TermsAndCondition> getTermsAndConditionBasedOnCountry(@Param("languageId") BigDecimal languageId,@Param("countryId") BigDecimal countryId);
	
}
