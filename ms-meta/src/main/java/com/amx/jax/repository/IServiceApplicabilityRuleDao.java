package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.ServiceApplicabilityRule;

public interface IServiceApplicabilityRuleDao extends JpaRepository<ServiceApplicabilityRule, Serializable> {

	@Query("select r from ServiceApplicabilityRule r where r.applicationCountryId =:applicationCountry "
			+ " and r.countryId =:beneCountry and r.currencyId=:currencyId and r.fieldName='BNFTELLAB' "
			+ " and r.mandatory='Y' and r.isActive='Y' ")
	public List<ServiceApplicabilityRule> getBeneTelServiceApplicabilityRule(
			@Param("applicationCountry") BigDecimal applicationCountry, @Param("beneCountry") BigDecimal beneCountry,
			@Param("currencyId") BigDecimal currencyId);
	
	@Query("select r from ServiceApplicabilityRule r where r.applicationCountryId =:applicationCountry "
			+ " and r.countryId =:beneCountry and r.currencyId=:currencyId and r.fieldName in (:fieldName) "
			+ " and r.isActive='Y' ")
	public List<ServiceApplicabilityRule> getServiceApplicabilityRules(
			@Param("applicationCountry") BigDecimal applicationCountry, @Param("beneCountry") BigDecimal beneCountry,
			@Param("currencyId") BigDecimal currencyId, @Param("fieldName") List<String> fieldName);

	@Query(nativeQuery=true,value= "Select b.* from EX_BENE_COUNTRY_SERVICE a, EX_SERVICE_APPLICABILITY_RULE b" + 
			" where a.APPLICATION_COUNTRY_ID = b.APPLICATION_COUNTRY_ID" + 
			" and a.BENE_COUNTRY_ID = b.COUNTRY_ID" + 
			" and a.CURRENCY_ID = b.CURRENCY_ID" + 
			" and a.REMITTANCE_MODE_ID = b.REMITTANCE_MODE_ID" + 
			" and a.DELIVERY_MODE_ID = b.DELIVERY_MODE_ID" + 
			" and a.ISACTIVE = 'Y'" + 
			" and b.ISACTIVE = 'Y'" + 
			" and b.APPLICATION_COUNTRY_ID=?1"+
			" and a.SERVICE_MASTER_ID <> 103" + 
			" and a.BENE_COUNTRY_ID = ?2" + 
			" and a.CURRENCY_ID = ?3"+
			" and b.FIELD_NAME = ?4")
	public List<ServiceApplicabilityRule> getServiceApplicabilityRulesForBeneficiary(BigDecimal applicationCountry,
			BigDecimal beneCountry, BigDecimal currencyId, String fieldName);
	
	

	@Query("select r from ServiceApplicabilityRule r where r.applicationCountryId =:applicationCountry "
			+ " and r.countryId =:beneCountry and r.currencyId=:currencyId and r.fieldName=:fieldName "
			+ " and r.remittanceModeId=:remittanceModeId and r.deliveryModeId=:deliveryModeId")
	public ServiceApplicabilityRule getServiceApplicabilityRulesForBank(
			@Param("applicationCountry") BigDecimal applicationCountry, @Param("beneCountry") BigDecimal beneCountry,
			@Param("currencyId") BigDecimal currencyId, @Param("fieldName") String fieldName,
			@Param("remittanceModeId") BigDecimal remittanceModeId,@Param("deliveryModeId") BigDecimal deliveryModeId);
	
	
	@Query("select r from ServiceApplicabilityRule r where r.applicationCountryId =:applicationCountry "
			+ " and r.countryId =:beneCountry and r.currencyId=:currencyId and r.fieldName=:fieldName "
			+ " and r.remittanceModeId=:remittanceModeId and r.deliveryModeId=:deliveryModeId")
	public ServiceApplicabilityRule getServiceApplicabilityRulesForBranchAndSwift(
			@Param("applicationCountry") BigDecimal applicationCountry, @Param("beneCountry") BigDecimal beneCountry,
			@Param("currencyId") BigDecimal currencyId, @Param("fieldName") String fieldName,
			@Param("remittanceModeId") BigDecimal remittanceModeId,@Param("deliveryModeId") BigDecimal deliveryModeId);
	
}
