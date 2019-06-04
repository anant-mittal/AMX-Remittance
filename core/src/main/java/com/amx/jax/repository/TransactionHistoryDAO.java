/**
 * Author :Radhika Ala
 * Date   :30/04/2019
 * Purpose : Trnx History in new database
 */

package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionHistoryView;


public interface TransactionHistoryDAO extends JpaRepository<CustomerRemittanceTransactionHistoryView, Serializable> {
	
	@Query("select th from CustomerRemittanceTransactionHistoryView th where th.customerId=:customerId  "
			+ "and trunc(th.documentDate) between  trunc(sysdate-6*30) and  trunc(sysdate) and th.beneficaryCorespondingBankName NOT IN('WU',' ') order by th.documentDate desc")
	public List<CustomerRemittanceTransactionHistoryView> getTransactionHistroy(@Param("customerId") BigDecimal customerId);
	
	@Query("select th from CustomerRemittanceTransactionHistoryView th where th.customerId=:customerId  and th.documentFinanceYear=:docfyr and th.documentNumber=:docNumber  ")
	public List<CustomerRemittanceTransactionHistoryView> getTransactionHistroyByDocumnet(
			@Param("customerId") BigDecimal customerId, @Param("docfyr") BigDecimal remittancedocfyr,
			@Param("docNumber") BigDecimal remittancedocNumber);
	
	@Query(value = " select * from HIS_JAX_VW_EX_TRANSACTION_INQ  where CUSTOMER_ID=?1 and DOCUMENT_FINANCE_YEAR=?2 "
			+ "and trunc(DOCUMENT_DATE) between to_date(?3,'dd/mm/yyyy') and to_date(?4,'dd/mm/yyyy') ORDER BY DOCUMENT_DATE DESC ", nativeQuery = true)
	public List<CustomerRemittanceTransactionHistoryView> getTransactionHistroyDocfyrAndDateWise(BigDecimal customerId,
			BigDecimal docfyr, String fromDate, String toDate);
	
	@Query(value = " select * from HIS_JAX_VW_EX_TRANSACTION_INQ where CUSTOMER_ID=?1 "
			+ "and trunc(DOCUMENT_DATE) between to_date(?2,'dd/mm/yyyy') and to_date(?3,'dd/mm/yyyy') ORDER BY DOCUMENT_DATE DESC ", nativeQuery = true)
	public List<CustomerRemittanceTransactionHistoryView> getTransactionHistoryDateWise(BigDecimal customerId, String fromDate, String toDate);

}
