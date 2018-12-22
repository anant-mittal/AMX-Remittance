package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CountryBranch;

@Transactional
public interface CountryBranchRepository extends CrudRepository<CountryBranch, BigDecimal> {

	public CountryBranch findBybranchName(String branchName);
	
	public CountryBranch findByBranchId(BigDecimal branchId);
	
	public CountryBranch findByCountryBranchId(BigDecimal countryBranchId);
}
