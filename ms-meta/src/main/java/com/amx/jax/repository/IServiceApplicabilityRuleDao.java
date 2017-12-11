package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.ServiceApplicabilityRule;

public interface IServiceApplicabilityRuleDao extends JpaRepository<ServiceApplicabilityRule, Serializable>{

	@Query("select r from ServiceApplicabilityRule r where r.applicationCountryId =:applicationCountry "
			+ "and r.countryId =:beneCountry and r.currencyId=:currencyId and r.fieldName='BNFTELLAB' "
			+ " and r.mandatory='Y' and r.isActive='Y' ")
	public List<ServiceApplicabilityRule> getServiceApplicabilityRule(
			@Param("applicationCountry") BigDecimal applicationCountry,
			@Param("beneCountry") BigDecimal beneCountry,
			@Param("currencyId") BigDecimal currencyId);
}
