package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ApplicationSetup;


public interface IApplicationCountryRepository extends JpaRepository<ApplicationSetup, BigDecimal>{
	
	@Query("Select a from ApplicationSetup a where  companyId=?1 or applicationCountryId=?2")
	List<ApplicationSetup> findByCountryIdAndCompanyId(BigDecimal companyId,BigDecimal applicationCountryId);
	
	@Query("Select a from ApplicationSetup a")
	ApplicationSetup getApplicationSetupDetails();
	
	
	@Query("Select a from ApplicationSetup a where  companyId=?1 or applicationCountryId=?2")
	ApplicationSetup findByCountryIdAndCompanyIdDeatils(BigDecimal companyId,BigDecimal applicationCountryId);
	

}
