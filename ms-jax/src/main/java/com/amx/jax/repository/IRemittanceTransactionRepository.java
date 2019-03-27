package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

public interface IRemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, Serializable> {
	
	public List<RemittanceTransaction> findByCollectionDocIdAndCollectionDocFinanceYearAndCollectionDocumentNo(BigDecimal collectionDocId,BigDecimal collectionDocFinanceYear,BigDecimal collectionDocumentNo);

}
