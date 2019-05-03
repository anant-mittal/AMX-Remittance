package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.CountryBranch;

@Transactional
public interface CountryBranchRepository extends CrudRepository<CountryBranch, BigDecimal> {

	public CountryBranch findBybranchName(String branchName);
	
	public CountryBranch findByBranchId(BigDecimal branchId);
	
	public CountryBranch findByCountryBranchId(BigDecimal countryBranchId);
	
	@Query("select c from CountryBranch c where c.isActive ='Y'")
	public List<CountryBranch> getCountryBranchList();
	
	@Query("select c from CountryBranch c where c.countryBranchId =?1")
	public List<CountryBranch> getCountryBranchIdList(BigDecimal countryBranchId);
}
