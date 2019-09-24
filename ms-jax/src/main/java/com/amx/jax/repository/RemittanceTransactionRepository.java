package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

@Transactional
public interface RemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, BigDecimal> {

	RemittanceTransaction findByapplicationDocumentNoAndApplicationFinanceYear(BigDecimal documentNo,BigDecimal documentFinanceYear);

	RemittanceTransaction findByDocumentNoAndDocumentFinanceYear(BigDecimal documentNo, BigDecimal documentFinanceYear);
	
	RemittanceTransaction findByRemittanceTransactionId(BigDecimal remittanceTransactionid);
	
	List<RemittanceTransaction> findByCustomerIdAndPaygTrnxDetailIdAndIsactive(Customer customerId,BigDecimal paygTrnxDetailId,String isactive);

}



