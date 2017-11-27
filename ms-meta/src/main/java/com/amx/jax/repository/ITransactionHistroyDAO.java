package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;

public interface ITransactionHistroyDAO extends JpaRepository<CustomerRemittanceTransactionView, Serializable> {

	/*@Query("select th from CustomerRemittanceTransactionView th where th.customerReference=:cutomerReference and th.documentFinanceYear=:docfyr or th.documentNumber=:docNumber" )
	public List<CustomerRemittanceTransactionView> getTransactionHistroyWithNamed(
			@Param("cutomerReference") BigDecimal cutomerReference, @Param("docfyr") BigDecimal docfyr,
			@Param("docNumber") BigDecimal docNumber, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);
	*/
	@Query("select th from CustomerRemittanceTransactionView th where th.customerReference=?1 and th.documentFinanceYear=?2 or th.documentNumber=?3 or th.documentDate between ?4 and ?5")
	public List<CustomerRemittanceTransactionView> getTransactionHistroy(
			BigDecimal cutomerReference,  BigDecimal docfyr,
			 BigDecimal docNumber,String fromDate,
			 String toDate);


}
