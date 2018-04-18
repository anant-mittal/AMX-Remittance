package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CountryBranch;

@Transactional
public interface CountryBranchRepository extends CrudRepository<CountryBranch, BigDecimal> {

	public CountryBranch findBybranchName(String branchName);
	
	public CountryBranch findByBranchId(BigDecimal branchId);
}
