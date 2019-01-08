package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.bene.BeneficaryRelationship;

public interface IBeneficiaryRelationshipDao extends JpaRepository<BeneficaryRelationship, Serializable>{
	
	@Query("select br from BeneficaryRelationship br where br.beneficaryRelationshipId=:beneRelationshipId "
			+ "and br.customerId = :customerId and br.isActive='Y'")
	public BeneficaryRelationship getBeneRelationshipById(
			@Param("beneRelationshipId") BigDecimal beneRelationshipId,
			@Param("customerId") BigDecimal customerId);
	
	
	@Query("select br from BeneficaryRelationship br where br.beneficaryMasterId=:beneMasterId "
			+ "and br.customerId = :customerId and br.isActive='Y'")
	public List<BeneficaryRelationship> getBeneRelationshipByBeneMasterId(
			@Param("beneMasterId") BigDecimal beneMasterId,
			@Param("customerId") BigDecimal customerId);
	
	
	@Query("select br from BeneficaryRelationship br where br.beneficaryMasterId=:beneMasterId "
			+ "and br.customerId = :customerId and br.isActive='Y' and br.orsSatus<>0")
	public List<BeneficaryRelationship> getBeneRelationshipByBeneMasterIdForDisable(
			@Param("beneMasterId") BigDecimal beneMasterId,
			@Param("customerId") BigDecimal customerId);
	
	@Query("select br from BeneficaryRelationship br where br.beneficaryMasterId=:beneMasterId "
			+ "and br.customerId = :customerId and br.isActive='D' and br.orsSatus<>0")
	public List<BeneficaryRelationship> getBeneRelationshipByBeneMasterIdForEnable(
			@Param("beneMasterId") BigDecimal beneMasterId,
			@Param("customerId") BigDecimal customerId);

	public List<BeneficaryRelationship> findByBeneficaryMasterIdAndBeneficaryAccountIdAndCustomerId(
			BigDecimal beneMasterId, BigDecimal beneAccountId, BigDecimal customerId);
	
	public List<BeneficaryRelationship> findByBeneficaryMasterIdAndBeneficaryAccountIdAndCustomerIdAndRelationsId(
			BigDecimal beneMasterId, BigDecimal beneAccountId, BigDecimal customerId, BigDecimal relationsId);
}
