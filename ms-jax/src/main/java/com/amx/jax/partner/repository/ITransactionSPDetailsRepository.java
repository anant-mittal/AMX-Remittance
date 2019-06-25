package com.amx.jax.partner.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.partner.TransactionDetailsView;

public interface ITransactionSPDetailsRepository extends CrudRepository<TransactionDetailsView, BigDecimal> {
	
	@Query(value = "SELECT * FROM VW_REMIT_TRNX_SRV_DETAIL WHERE COLLECTION_DOC_FINANCE_YEAR=?1 AND COLLECTION_DOC_NO=?2", nativeQuery = true)
	public List<TransactionDetailsView> fetchTrnxSPDetails(BigDecimal colDocumentFinanceYear,BigDecimal colDocumentNo);

}
