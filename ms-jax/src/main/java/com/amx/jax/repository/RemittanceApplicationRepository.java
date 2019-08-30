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

	@Query("select ra from RemittanceApplication ra where ra.fsCustomer=:customerId and ra.paymentId=:paymentId")
	public List<RemittanceApplication> fetchRemitApplTrnxRecordsByCustomerPayId(@Param("paymentId") String paymentId,
			@Param("customerId") Customer customerId);

	@Query("select rv from RemittanceTransactionView rv where rv.applicationDocumentNo=?1 and rv.applicationFinancialYear = ?2")
	public RemittanceTransactionView fetchRemitApplTrnxView(BigDecimal applicationDocumentNo, BigDecimal docFinYear);

	@Query("select rv from RemittanceApplication rv where rv.documentNo=?1 and rv.documentFinancialyear = ?2")
	public RemittanceApplication fetchRemitApplTrnx(BigDecimal applicationDocumentNo, BigDecimal docFinYear);
	
	@Query(value = "select count(*) from EX_appl_trnx where customer_id = ?1 and CREATED_DATE between sysdate - interval '30' minute and sysdate "
			+ "and RESULT_CODE != 'CAPTURED'", nativeQuery = true)
	public Long getFailedTransactionAttemptCount(BigDecimal customerId);
	
	
	@Query("select ra from RemittanceApplication ra where ra.fsCustomer=:customerid and ra.remittanceApplicationId=:remittanceApplicationId and ra.isactive='Y' and  trunc(sysdate)=trunc(createdDate)")
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
	
    @Transactional
	@Modifying
	@Query("update RemittanceApplication appl set isactive = 'D',applicaitonStatus = null where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) " 
			+"and isactive <> 'D' and NVL(resultCode,' ') NOT IN('CAPTURED','APPROVED') and appl.loccod =:locCod and paymentType <> 'WT'")
	public void deActivateNotUsedOnlineApplication(@Param("customerId") Customer customerId,@Param("locCod") BigDecimal locCod);
    
    @Transactional
	@Modifying
	@Query("update RemittanceApplication appl set isactive = 'D',applicaitonStatus = null where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) " 
			+"and isactive <> 'D' and NVL(resultCode,' ') NOT IN('CAPTURED','APPROVED')")
	public void deActivateNotUsedAllApplication(@Param("customerId") Customer customerId);
	 
}
