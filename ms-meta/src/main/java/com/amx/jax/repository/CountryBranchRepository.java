package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.CountryBranchMdlv1;

@Transactional
public interface CountryBranchRepository extends CrudRepository<CountryBranchMdlv1, BigDecimal> {

	public CountryBranchMdlv1 findBybranchName(String branchName);
	
	public CountryBranchMdlv1 findByBranchId(BigDecimal branchId);
	
	public CountryBranchMdlv1 findByCountryBranchId(BigDecimal countryBranchId);
	
	@Query("select c from CountryBranchMdlv1 c where c.isActive ='Y'")
	public List<CountryBranchMdlv1> getCountryBranchList();
	
	@Query("select c from CountryBranchMdlv1 c where c.countryBranchId =?1")
	public List<CountryBranchMdlv1> getCountryBranchIdList(BigDecimal countryBranchId);
}
