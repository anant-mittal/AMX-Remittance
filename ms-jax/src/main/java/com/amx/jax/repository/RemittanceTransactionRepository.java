package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

@Transactional
public interface RemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, BigDecimal> {

	RemittanceTransaction findByapplicationDocumentNoAndApplicationFinanceYear(BigDecimal docNo,BigDecimal finYear);

	RemittanceTransaction findByDocumentNoAndDocumentFinanceYear(BigDecimal documentNo, BigDecimal docFinYear);
	
	RemittanceTransaction findByRemittanceTransactionId(BigDecimal remittanceTransactionid);

}



