package com.amx.jax.complaince;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface RemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, BigDecimal> {

	@Query("select remittanceTransactionId from RemittanceTransaction rt where rt.documentNo=?1  and rt.documentFinanceYear=?2")
	List<RemittanceTransaction> findByDocumentNoAndDocumentFinanceYear(BigDecimal documentNo,BigDecimal documentFinanceYear);
}

