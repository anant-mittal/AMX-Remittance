package com.amx.jax.partner.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.partner.TransactionDetailsView;

public interface ITransactionSPDetailsRepository extends CrudRepository<TransactionDetailsView, BigDecimal> {
	
	@Query(value = "SELECT * FROM VW_REMIT_TRNX_SRV_DETAIL WHERE CUSTOMER_ID =?1 AND COLLECTION_DOC_FINANCE_YEAR=?2 AND COLLECTION_DOC_NO=?3 AND COLLECTION_DOC_CODE=?4 ", nativeQuery = true)
	public List<TransactionDetailsView> fetchTrnxSPDetails(BigDecimal customerId,BigDecimal colDocumentFinanceYear,BigDecimal colDocumentNo,BigDecimal colDocumentCode);
	
	@Query(value = "SELECT * FROM VW_REMIT_TRNX_SRV_DETAIL WHERE CUSTOMER_ID =?1 AND DOCUMENT_FINANCE_YEAR=?2 AND DOCUMENT_NO=?3", nativeQuery = true)
	public List<TransactionDetailsView> fetchTrnxWiseDetails(BigDecimal customerId,BigDecimal documentFinanceYear,BigDecimal documentNo);

	@Query(value = "SELECT * FROM VW_REMIT_TRNX_SRV_DETAIL WHERE CUSTOMER_ID =?1 AND DOCUMENT_FINANCE_YEAR=?2 "
			+ " AND DOCUMENT_NO=?3 AND COLLECTION_DOC_FINANCE_YEAR=?4 AND COLLECTION_DOC_NO=?5 AND COLLECTION_DOC_CODE=?6 ", nativeQuery = true)
	public List<TransactionDetailsView> fetchTrnxWiseDetailsForCustomer(BigDecimal customerId,BigDecimal documentFinanceYear,BigDecimal documentNo,
			BigDecimal colDocumentFinanceYear,BigDecimal colDocumentNo,BigDecimal colDocumentCode);
}
