package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;



public interface ITransactionHistroyDAO extends JpaRepository<CustomerRemittanceTransactionView, Serializable> {
	
	
	//and trunc(document_Date) between  trunc(sysdate-6*30) and  trunc(sysdate);
	// documentDate  --and th.documentFinanceYear=:docfyr
	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerId  "
			+ "and trunc(th.documentDate) between  trunc(sysdate-6*30) and  trunc(sysdate)") 
	public List<CustomerRemittanceTransactionView> getTransactionHistroy(
			@Param("customerId") BigDecimal customerId);
	
	
	@Query("select th from CustomerRemittanceTransactionView th where th.customerId=:customerId  and th.documentFinanceYear=:docfyr and th.documentNumber=:docNumber  ") 
	public List<CustomerRemittanceTransactionView> getTransactionHistroyByDocumnet(
			@Param("customerId") BigDecimal customerId, @Param("docfyr") BigDecimal docfyr,
			@Param("docNumber") BigDecimal docNumber);
	

	@Query(value=" select * from VW_EX_TRANSACTION_INQUIRY where CUSTOMER_ID=?1 and DOCUMENT_FINANCE_YEAR=?2 "
			+ "and DOCUMENT_DATE between to_date(?3,'dd/mm/yyyy') and to_date(?4,'dd/mm/yyyy') ",nativeQuery=true)
	public List<CustomerRemittanceTransactionView> getTransactionHistroyDateWise(
			BigDecimal customerId,  BigDecimal docfyr,
			 String fromDate, String toDate);

	
}
