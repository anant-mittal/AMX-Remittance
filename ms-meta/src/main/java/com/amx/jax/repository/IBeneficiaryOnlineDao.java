package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.BenificiaryListView;

public interface IBeneficiaryOnlineDao extends JpaRepository<BenificiaryListView, Serializable> {

	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId and bl.applicationCountryId=:applicationCountryId and orsStatus <> 0  ORDER BY bl.totalTrnx desc")
	public List<BenificiaryListView> getOnlineBeneListFromView(@Param("customerId") BigDecimal customerId,@Param("applicationCountryId") BigDecimal applicationCountryId);
	

	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId and bl.applicationCountryId=:applicationCountryId and benificaryCountry=:beneCountryId and orsStatus <> 0  ORDER BY bl.totalTrnx desc")
	public List<BenificiaryListView> getOnlineBeneListFromViewForCountry(@Param("customerId") BigDecimal customerId,@Param("applicationCountryId") BigDecimal applicationCountryId,@Param("beneCountryId") BigDecimal beneCountryId);
	
	
	
	
	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId and bl.applicationCountryId=:applicationCountryId")
	public List<BenificiaryListView> getBeneListFromView(@Param("customerId") BigDecimal customerId,
			@Param("applicationCountryId") BigDecimal applicationCountryId);
	
	
	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId and bl.applicationCountryId=:applicationCountryId and benificaryCountry=:beneCountryId")
	public List<BenificiaryListView> getBeneListFromViewForCountry(@Param("customerId") BigDecimal customerId,
			@Param("applicationCountryId") BigDecimal applicationCountryId,
			@Param("beneCountryId") BigDecimal beneCountryId);
	
	
}