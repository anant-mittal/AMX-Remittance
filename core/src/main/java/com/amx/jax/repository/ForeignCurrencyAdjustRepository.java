package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ForeignCurrencyAdjustMdlv1;

public interface ForeignCurrencyAdjustRepository extends CrudRepository<ForeignCurrencyAdjustMdlv1, String>{
	
	@Query(value = "SELECT * FROM EX_CURRENCY_ADJUST WHERE DOCUMENT_NO = ?1 AND DOCUMENT_FINANCE_YEAR = ?2 AND COMPANY_ID = ?3 AND DOCUMENT_CODE = ?4 AND STATUS = ?5", nativeQuery = true)
	public List<ForeignCurrencyAdjustMdlv1> fetchByCollectionDetails(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode,String status);
	
	@Query(value = "SELECT * FROM EX_CURRENCY_ADJUST WHERE DOCUMENT_NO = ?1 AND DOCUMENT_FINANCE_YEAR = ?2 AND COMPANY_ID = ?3 AND DOCUMENT_CODE = ?4 AND TRANSACTION_TYPE = ?5 AND STOCK_UPDATED = ?6 AND DOCUMENT_STATUS = ?7", nativeQuery = true)
	public List<ForeignCurrencyAdjustMdlv1> fetchByCollectionDetailsByTrnxType(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode,String tranctionType,String stockUpdate,String documentStatus);
	
	@Query(value = "SELECT * FROM EX_CURRENCY_ADJUST WHERE DOCUMENT_NO = ?1 AND DOCUMENT_FINANCE_YEAR = ?2 AND COMPANY_ID = ?3 AND DOCUMENT_CODE = ?4 AND DOCUMENT_STATUS = ?5", nativeQuery = true)
	public List<ForeignCurrencyAdjustMdlv1> fetchByDocumentDetails(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode,String documentStatus);

}
