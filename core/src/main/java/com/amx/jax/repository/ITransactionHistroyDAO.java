/**
 * Author :MRU
 * Date   :03/01/2018
 * Purpose : Trnx Hist
 */

package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.dbmodel.remittance.BeneTransactionCountModel;

public interface ITransactionHistroyDAO extends JpaRepository<CustomerRemittanceTransactionView, Serializable> {

	
	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerId  "
			+ "and trunc(th.documentDate) between  trunc(sysdate-6*30) and  trunc(sysdate) and th.beneficaryCorespondingBankName NOT IN('WU',' ') order by th.documentDate desc")
	public List<CustomerRemittanceTransactionView> getTransactionHistroy(@Param("customerId") BigDecimal customerId);

	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerId  and th.documentFinanceYear=:docfyr and th.documentNumber=:docNumber")
	public List<CustomerRemittanceTransactionView> getTransactionHistroyByDocumnet(
			@Param("customerId") BigDecimal customerId, @Param("docfyr") BigDecimal remittancedocfyr,
			@Param("docNumber") BigDecimal remittancedocNumber);

	@Query(value = "select * from JAX_VW_EX_TRANSACTION_INQUIRY where CUSTOMER_ID=?1 and DOCUMENT_FINANCE_YEAR=?2  " 
			+  "and trunc(DOCUMENT_DATE) between to_date(?3,'dd/mm/yyyy') and to_date(?4,'dd/mm/yyyy')  ORDER BY DOCUMENT_DATE DESC", nativeQuery = true)
	public List<CustomerRemittanceTransactionView> getTransactionHistroyDocfyrDateWise(BigDecimal customerId,
			BigDecimal docfyr, String fromDate, String toDate);
	
	@Query(value = "  select * from JAX_VW_EX_TRANSACTION_INQUIRY where CUSTOMER_ID=?1 "
			+ "and trunc(DOCUMENT_DATE) between to_date(?2,'dd/mm/yyyy') and to_date(?3,'dd/mm/yyyy') ORDER BY DOCUMENT_DATE DESC", nativeQuery = true)
	public List<CustomerRemittanceTransactionView> getTransactionHistroyDateWise(BigDecimal customerId, String fromDate, String toDate);

	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerid and th.beneficiaryRelationSeqId=:beneRelationId "
			+ " and TRUNC(th.documentDate)=(select MAX(TRUNC(thi.documentDate)) from CustomerRemittanceTransactionView thi "
			+ " where thi.customerId=:customerid and thi.beneficiaryRelationSeqId=:beneRelationId) and rownum=1" )
	public CustomerRemittanceTransactionView getDefaultTrnxHist(@Param("customerid") BigDecimal customerid,
			@Param("beneRelationId") BigDecimal beneRelationId);
	
	
	
	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerid  and th.beneficiaryRelationSeqId=:beneRelationId and th.idno=:transactionId")
	public CustomerRemittanceTransactionView getTrnxHistByBeneIdAndTranId(
			@Param("customerid") BigDecimal customerid, 
			@Param("beneRelationId") BigDecimal beneRelationId, 
			@Param("transactionId") BigDecimal transactionId);
	
	
	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerid  and th.idno=:transactionId")
	public CustomerRemittanceTransactionView getTrnxHistTranId(
			@Param("customerid") BigDecimal customerid, 
			@Param("transactionId") BigDecimal transactionId);
	


	@Query(value= "SELECT COUNT(*)  FROM  EX_APPL_TRNX A, EX_APPL_BENE B"
			+ "              WHERE A.CUSTOMER_ID  = ?1"
			+ "              AND   TRUNC(A.DOCUMENT_DATE)      =  TRUNC(SYSDATE)"
			+ "              AND   A.REMITTANCE_APPLICATION_ID =  B.REMITTANCE_APPLICATION_ID"
			//+ "              AND   A.TRANSACTION_DOCUMENT_NO   IS  NULL"
			+ "              AND   NVL(A.ISACTIVE,' ')         =  'Y'"
			+ "              AND   B.BENEFICIARY_ID            =  ?2", nativeQuery=true)
	public Integer getTodaysTransactionForBeneficiary(BigDecimal customerId, BigDecimal benRelationId);

	
	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerid and th.transactionStatusDesc='PAID'")
	public List<CustomerRemittanceTransactionView> getCustomerTotalTrnx(@Param("customerid") BigDecimal customerid);
	
	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerid and th.documentCode=:documentCode and th.transactionTypeDesc is not null order by th.documentDate desc")
	public List<CustomerRemittanceTransactionView> getLastTransaction(@Param("customerid") BigDecimal customerid,
			@Param("documentCode") BigDecimal documentCode, Pageable pageable);
	
	@Query("select count(th) from CustomerRemittanceTransactionView th where th.idno in ?1")
	public Long getCountByBenerelationshipSeqId(List<BigDecimal> idNo);
	
	
	@Query(value= "select last_trnx_amt from ( "
					+" select COLLECTION_DOCUMENT_NO,sum(LOCAL_NET_TRANX_AMOUNT) last_trnx_amt "
					+" from ex_remit_trnx                                                      "
					+" where ACCOUNT_MMYYYY =to_date(:accMyear,'dd/MM/yyyy') and     "
					+" country_branch_id =:countrybranchId and                              "
					+" CREATED_BY=:username                                                 "
					+" and trunc(sysdate)=trunc(created_Date)                                  "
					+" group by COLLECTION_DOCUMENT_NO                                         "
					+" order by COLLECTION_DOCUMENT_NO  desc ) where rownum = 1" , nativeQuery=true)
	public BigDecimal  getLastTrnxAmountFortheUser(@Param("username") String username,@Param("accMyear") String accMyear,@Param("countrybranchId") BigDecimal countrybranchId);
	

	//	to fetch trnx histroy	
	public List<CustomerRemittanceTransactionView> findByCustomerIdAndCollectionDocumentFinYearAndCollectionDocumentNoAndCollectionDocumentCode(BigDecimal customerId,BigDecimal collectionDocumentFinYear,BigDecimal CollectionDocumentNo,BigDecimal collectionDocumentCode); 

	@Query(value = "select new com.amx.jax.dbmodel.remittance.BeneTransactionCountModel(th.beneficiaryRelationSeqId, count(*) ) from CustomerRemittanceTransactionView th where beneficiaryRelationSeqId in (?1) group by beneficiaryRelationSeqId")
	public List<BeneTransactionCountModel> getTransactionCountByBeneRelSeqId(List<BigDecimal> beneRelSeqIds);
	
	@Query(value = "select distinct(beneficary_relationship_seq_id) from JAX_VW_EX_TRANSACTION_INQUIRY where beneficary_relationship_seq_id in (?1) ", nativeQuery = true)
	public List<BigDecimal> getbeneRelSeqlIdsTranaction(List<BigDecimal> beneRelSeqIds);

}
