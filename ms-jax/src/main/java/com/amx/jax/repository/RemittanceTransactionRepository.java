package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.amx.jax.dbmodel.Customer;

import com.amx.jax.dbmodel.ReceiptPayment;

import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

@Transactional
public interface RemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, BigDecimal> {

	RemittanceTransaction findByapplicationDocumentNoAndApplicationFinanceYear(BigDecimal documentNo,BigDecimal documentFinanceYear);

	RemittanceTransaction findByDocumentNoAndDocumentFinanceYear(BigDecimal documentNo, BigDecimal documentFinanceYear);
	
	RemittanceTransaction findByRemittanceTransactionId(BigDecimal remittanceTransactionid);
	List<RemittanceTransaction> findByCustomerIdAndPaygTrnxDetailIdAndIsactive(Customer customerId,BigDecimal paygTrnxDetailId,String isactive);

		
	@Query(value = "SELECT * FROM EX_REMIT_TRNX WHERE CUSTOMER_ID=?1  AND ((CREATED_BY = 'ANDROID') OR (CREATED_BY = 'IOS') OR (CREATED_BY = 'ONLINE'))", nativeQuery = true)
	public List<RemittanceTransaction> getTransactionMadeByOnline(String customerId);


}



