package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

@Transactional
public interface RemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, BigDecimal> {

	RemittanceTransaction findByapplicationDocumentNoAndApplicationFinanceYear(BigDecimal documentNo,BigDecimal documentFinanceYear);

	RemittanceTransaction findByDocumentNoAndDocumentFinanceYear(BigDecimal documentNo, BigDecimal documentFinanceYear);
	
	RemittanceTransaction findByRemittanceTransactionId(BigDecimal remittanceTransactionid);
	List<RemittanceTransaction> findByCustomerIdAndPaygTrnxDetailIdAndIsactive(Customer customerId,BigDecimal paygTrnxDetailId,String isactive);

		
	@Query(value = "SELECT * FROM EX_REMIT_TRNX WHERE CUSTOMER_ID=?1  AND ((CREATED_BY = 'ANDROID') OR (CREATED_BY = 'IOS') OR (CREATED_BY = 'ONLINE'))", nativeQuery = true)
	public List<RemittanceTransaction> getTransactionMadeByOnline(String customerId);
	
	List<RemittanceTransaction> findByCollectionDocFinanceYearAndCollectionDocumentNo(BigDecimal collectionDocFinanceYear,BigDecimal collectionDocumentNo);
	
	@Query("select c from RemittanceTransaction c where documentNo=?1 and documentFinanceYear=?2")
	public List<RemittanceTransaction> getRemittanceTransaction(BigDecimal documentNo, BigDecimal documentFinanceYear);
	@Query(value="SELECT * FROM EX_REMIT_TRNX a WHERE CUSTOMER_ID=?1 and a.COUNTRY_BRANCH_ID =?2 and trunc(a.DOCUMENT_DATE)=trunc(SYSDATE)" , nativeQuery = true)
	public List<RemittanceTransaction> getTotalTrnxCntForCustomer(BigDecimal customerid,BigDecimal cntryBranchid);


}



