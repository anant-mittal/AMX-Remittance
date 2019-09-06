package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.RemittanceTransactionView;

/**
 * 
 * @author :Rabil
 * Date    :01/12/2016
 *
 */

public interface IRemittanceTransactionDao extends JpaRepository<RemittanceTransactionView, Serializable>{

	
	@Query("select rtv from RemittanceTransactionView rtv where rtv.collectionDocumentNo=:documentNo and rtv.applicationFinancialYear=:fYear and rtv.collectionDocCode=:documentCode")
	public List<RemittanceTransactionView> getRemittanceTransaction(@Param("documentNo") BigDecimal documentNo,@Param("fYear") BigDecimal fYear,@Param("documentCode") BigDecimal documentCode);

	public RemittanceTransactionView findByRemittanceTransactionId(BigDecimal remittanceTransactionId);
	
	@Query("select rtv from RemittanceTransactionView rtv where rtv.collectionDocumentNo=:documentNo and rtv.applicationFinancialYear=:fYear and rtv.collectionDocCode=:documentCode and "
			+ " rtv.idProofTypeId=:idProofTypeId")
	public List<RemittanceTransactionView> getRemittanceTransactionForReport(@Param("documentNo") BigDecimal documentNo,
			@Param("fYear") BigDecimal fYear, @Param("documentCode") BigDecimal documentCode,
			@Param("idProofTypeId") BigDecimal idProofTypeId);
	
	@Query("select rtv from RemittanceTransactionView rtv where rtv.collectionDocumentNo=:documentNo and rtv.applicationFinancialYear=:fYear and rtv.collectionDocCode=:documentCode and rtv.bankId=:bankId")
	public List<RemittanceTransactionView> getRemittanceTransactionByRoutingBank(@Param("documentNo") BigDecimal documentNo,
			@Param("fYear") BigDecimal fYear,@Param("documentCode") BigDecimal documentCode,@Param("bankId") BigDecimal bankId);
}
