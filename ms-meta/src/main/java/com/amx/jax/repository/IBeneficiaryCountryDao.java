package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.BeneficiaryCountryView;

public interface IBeneficiaryCountryDao extends JpaRepository<BeneficiaryCountryView, Serializable> {

	@Query("select DISTINCT cl from BeneficiaryCountryView cl where cl.customerId=:customerId  and orsStatus<>0 ORDER BY countryName asc ")
	public List<BeneficiaryCountryView> getBeneCountryForOnline(@Param("customerId") BigDecimal customerId);

	@Query("select DISTINCT cl from BeneficiaryCountryView cl where cl.customerId=:customerId ORDER BY countryName asc ")
	public List<BeneficiaryCountryView> getBeneCountryForBranch(@Param("customerId") BigDecimal customerId);

	public List<BeneficiaryCountryView> findByCustomerIdAndBeneCountry(BigDecimal customerId, BigDecimal beneCountryId);
}
