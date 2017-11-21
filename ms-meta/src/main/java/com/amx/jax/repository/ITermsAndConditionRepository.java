package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.model.TermsAndCondition;

public interface ITermsAndConditionRepository<T> extends JpaRepository<TermsAndCondition,BigDecimal>{
	
	@Query("Select t from TermsAndCondition t where languageId=?")
	public List<TermsAndCondition> getTermsAndCondition(BigDecimal languageId);
	
}
