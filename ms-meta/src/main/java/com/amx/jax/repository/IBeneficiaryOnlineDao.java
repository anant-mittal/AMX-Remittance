package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
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
	
	
	
	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerid "
			+ "and bl.applicationCountryId=:applicationCountryId "
			+ "and bl.totalTrnx = (select max(ibl.totalTrnx) from BenificiaryListView ibl where ibl.customerId=:customerid) and rownum=1")
	public BenificiaryListView getDefaultBeneficiary(@Param("customerid") BigDecimal customerid,
			@Param("applicationCountryId") BigDecimal applicationCountryId);
	
	
	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId "
			+ "and bl.applicationCountryId=:applicationCountryId and bl.beneficiaryRelationShipSeqId =:beneRelationId and bl.orsStatus <>0")
	public BenificiaryListView getBeneficiaryByRelationshipId(@Param("customerId") BigDecimal customerId,
			@Param("applicationCountryId") BigDecimal applicationCountryId,
			@Param("beneRelationId") BigDecimal beneRelationId);
	
	
	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId and bl.applicationCountryId=:applicationCountryId and myFavouriteBene='Y' and isActive='Y' and orsStatus <> 0  ORDER BY bl.totalTrnx desc")
	public List<BenificiaryListView> getFavouriteBeneListFromViewForCountry(@Param("customerId") BigDecimal customerId,@Param("applicationCountryId") BigDecimal applicationCountryId);

	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId and bl.applicationCountryId=:applicationCountryId and orsStatus <> 0 and bl.lastJavaRemittance is not null ORDER BY bl.lastJavaRemittance desc")
	public List<BenificiaryListView> getLastTransactionBene(@Param("customerId") BigDecimal customerId,@Param("applicationCountryId") BigDecimal applicationCountryId, Pageable pageble);
	
	public BenificiaryListView findBybeneficiaryRelationShipSeqId(BigDecimal beneficiaryRelationShipSeqId);
	
	@Query("select bl from BenificiaryListView bl where bl.customerId=:customerId and orsStatus <> 0 and bl.beneficiaryRelationShipSeqId in (:beneficiaryRelationShipSeqIds)")
	public List<BenificiaryListView> getBeneficiaryRelationShipSeqIds(@Param("customerId") BigDecimal customerId, @Param("beneficiaryRelationShipSeqIds") List<BigDecimal> beneficiaryRelationShipSeqIds);
	
	@Query(value= "select b1.* from JAX_VW_LIST_BENEFICIARY b1 where b1.BANK_ID not in (select ROUTING_BANK_ID from ex_routing_header)", nativeQuery=true)
	public List<BenificiaryListView> listBeneficiaryForPOloadTest();
	
	public List<BenificiaryListView> findByIsActiveAndCurrencyIdAndBankIdNotIn(String isActive, BigDecimal currencyId,List<BigDecimal> bankIds, Pageable pageable);

	
	public BenificiaryListView findByCustomerIdAndBeneficiaryRelationShipSeqId(BigDecimal customerId,BigDecimal beneficiaryRelationShipSeqId);
}
