package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;


public interface RemittanceApplicationRepository extends CrudRepository<RemittanceApplication, BigDecimal> {

	@Query("select ra from RemittanceApplication ra where ra.paymentId=:paymentId")
	public List<RemittanceApplication> fetchRemitApplTrnxRecordsByPayId(@Param("paymentId") String paymentId);

	@Query("select appl from RemittanceApplication appl where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) "
			+ "  and isactive <> 'D' and NVL(resultCode,' ') NOT IN('CAPTURED','APPROVED')")
	public List<RemittanceApplication> deActivateNotUsedApplication(@Param("customerId") Customer customerId);

	@Query("select ra from RemittanceApplication ra where ra.fsCustomer=:customerId and ra.paymentId=:paymentId and ra.isactive='Y'")
	public List<RemittanceApplication> fetchRemitApplTrnxRecordsByCustomerPayId(@Param("paymentId") String paymentId,@Param("customerId") Customer customerId);
	
	
	@Query("select ra from RemittanceApplication ra where ra.fsCustomer=:customerId and ra.paygTrnxDetailId=:paymentId and ra.isactive='Y' and trunc(sysdate)=trunc(createdDate)")
	public List<RemittanceApplication> fetchRemitApplTrnxRecordsByCustomerPaygDetailId(@Param("paymentId") BigDecimal paymentId,@Param("customerId") Customer customerId);
	

	@Query("select rv from RemittanceTransactionView rv where rv.applicationDocumentNo=?1 and rv.applicationFinancialYear = ?2")
	public RemittanceTransactionView fetchRemitApplTrnxView(BigDecimal applicationDocumentNo, BigDecimal docFinYear);

	@Query("select rv from RemittanceApplication rv where rv.documentNo=?1 and rv.documentFinancialyear = ?2")
	public RemittanceApplication fetchRemitApplTrnx(BigDecimal applicationDocumentNo, BigDecimal docFinYear);
	
	@Query(value = "select count(*) from EX_appl_trnx where customer_id = ?1 and CREATED_DATE between sysdate - interval '30' minute and sysdate "
			+ "and RESULT_CODE != 'CAPTURED'", nativeQuery = true)
	public Long getFailedTransactionAttemptCount(BigDecimal customerId);
	
	
	@Query("select ra from RemittanceApplication ra where ra.fsCustomer=:customerid and ra.remittanceApplicationId=:remittanceApplicationId and ra.isactive='Y' and  trunc(sysdate)=trunc(createdDate)  "
			+ "and NVL(applicaitonStatus,' ') <>'T'  and NVL(transactionDocumentNo,0)=0")
	public RemittanceApplication getApplicationForRemittance(@Param("customerid") Customer customerid,@Param("remittanceApplicationId") BigDecimal remittanceApplicationId);
	
	
	 @Query("update RemittanceApplication a set a.transactionDocumentNo = :docNo,a.transactionFinancialyear=:docFyr ,a.exUserFinancialYearByTransactionFinanceYearID =:docFyrId , "
	 		+ " a.applicaitonStatus ='T' WHERE a.fsCustomer=:customerId and a.remittanceApplicationId =:remittanceApplicationId")
	 public void updateApplicationDetails(@Param("customerId") Customer customerId,@Param("remittanceApplicationId") BigDecimal remittanceApplicationId,
			 @Param("docFyr") BigDecimal docFyr,@Param("docNo") BigDecimal docNo,@Param("docFyrId") UserFinancialYear docFyrId);

	
	 @Query("select ra from RemittanceApplication ra where ra.fsCustomer=:customerid and ra.remittanceApplicationId=:remittanceApplicationId")
		public RemittanceApplication getApplicationForDelete(@Param("customerid") Customer customerid,@Param("remittanceApplicationId") BigDecimal remittanceApplicationId);
		
	 
	 @Query("select ra from RemittanceApplication ra where ra.fsCustomer=:customerid and ra.documentNo=:applicationDocumentNo and ra.documentFinancialyear = :docFinYear")
		public RemittanceApplication getApplicationDetailsForUpdate(@Param("customerid") Customer customerid,
				@Param("applicationDocumentNo") BigDecimal applicationDocumentNo, @Param("docFinYear") BigDecimal docFinYear);
	 
	 @Query("select ra from RemittanceApplication ra where ra.documentNo=?1 and ra.documentFinancialyear = ?2")
		public RemittanceApplication getRemittanceApplicationId(BigDecimal applicationDocumentNo, BigDecimal docFinYear);
	 
	/*@Query("update RemittanceApplication appl set isactive = 'D', applicaitonStatus = null where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) "
			+ "and isactive <> 'D' and NVL(resultCode,' ') NOT IN('CAPTURED','APPROVED') and appl.loccod =:locCod")
	public void deActivateNotUsedOnlineApplication(@Param("customerId") Customer customerId,@Param("locCod") BigDecimal locCod);*/
	 @Query("select rv from RemittanceApplication rv where rv.remittanceApplicationId=?1")
		public RemittanceApplication fetchByRemittanceApplicationId(BigDecimal remittanceApplicationId);
	
    @Transactional
	@Modifying
	@Query("update RemittanceApplication appl set isactive = 'D',applicaitonStatus = null where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) " 
			+"and isactive <> 'D' and NVL(resultCode,' ') NOT IN('CAPTURED','APPROVED') and appl.loccod =:locCod and NVL(paymentType,' ') <> 'PB'")
	public void deActivateNotUsedOnlineApplication(@Param("customerId") Customer customerId,@Param("locCod") BigDecimal locCod);
    
    @Transactional
	@Modifying
	@Query("update RemittanceApplication appl set isactive = 'D',applicaitonStatus = null where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) " 
			+"and isactive <> 'D' and NVL(resultCode,' ') NOT IN('CAPTURED','APPROVED') and NVL(PAYMENT_TYPE,' ') <> 'PB'")
	public void deActivateNotUsedAllApplication(@Param("customerId") Customer customerId);
    

    @Query("select ra from RemittanceApplication ra where ra.fsCustomer = ?1 and ra.paymentType = ?2 and ra.isactive='Y' and ra.createdDate=sysDate order by createdDate desc")
    public List<RemittanceApplication> getLatestPbApplication(Customer customerId , String paymentType);
    
    @Transactional
	@Modifying
	@Query("update RemittanceApplication appl set isactive = 'D',applicaitonStatus = null where appl.remittanceApplicationId=?1")
	public void deActivateLatestPbApplication(BigDecimal remittanceApplicationId);

    @Query("select ra from RemittanceApplication ra where ra.paymentLinkId=:paymentLinkId")
	public List<RemittanceApplication> getApplByPaymentlinkId(@Param("paymentLinkId") BigDecimal paymentLinkId);
    
    @Transactional
	@Modifying
	@Query("update RemittanceApplication appl set paymentLinkId = :paymentLinkId where appl.remittanceApplicationId=:remittanceApplicationId")
	public void updateLinkId(@Param("remittanceApplicationId") BigDecimal remittanceApplicationId, @Param("paymentLinkId") BigDecimal paymentLinkId);

    @Query("select ra from RemittanceApplication ra where ra.remittanceApplicationId in (?1)")
	public List<RemittanceApplication> getApplicationList(List<BigDecimal> appIdsBigDecimalList);
    
	public List<RemittanceApplication> findByFsCustomerAndPaygTrnxDetailId(Customer fsCustomer, BigDecimal paygTrnxDetailId);
	
	@Query(value =" SELECT * FROM EX_APPL_TRNX A WHERE a.CUSTOMER_ID=?1 AND a.ACCOUNT_MMYYYY=trunc(SYSDATE,'MONTH') AND a.ISACTIVE='Y' "+ 
			" and a.COUNTRY_BRANCH_ID=?2 and TRUNC(SYSDATE)=TRUNC(A.CREATED_DATE) and NVL(APPLICATION_STATUS,' ' ) <>'T' ",nativeQuery = true)
	public List<RemittanceApplication> getApplicationCountList(BigDecimal customerId,BigDecimal countryBranchId);
	
	
	@Transactional
	@Modifying
	@Query("update RemittanceApplication appl set isactive = 'D',applicaitonStatus = null where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) " 
			+"and isactive <> 'D' and appl.loccod <>:locCod and NVL(applicaitonStatus,' ')<>'T' and transactionDocumentNo is null")
	public void deActivateBranchApplicationInOnline(@Param("customerId") Customer customerId,@Param("locCod") BigDecimal locCod);
	
}
