package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.TermsAndCondition;

public interface ITermsAndConditionRepository<T> extends JpaRepository<TermsAndCondition,BigDecimal>{
	
	@Query("Select t from TermsAndCondition t where languageId=?1 and moduleType=?2 and status=?3")
	public List<TermsAndCondition> getTermsAndCondition(BigDecimal languageId,String moduleType,String status);
	
	@Query("Select t from TermsAndCondition t where languageId=:languageId and countryId=:countryId and moduleType=:moduleType and status=:status")
	public List<TermsAndCondition> getTermsAndConditionBasedOnCountry(@Param("languageId") BigDecimal languageId,@Param("countryId") BigDecimal countryId,@Param("moduleType") String moduleType,@Param("status") String status);
	
}
