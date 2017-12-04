package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CollectionPaymentDetailsViewModel;

public interface ICollectionPaymentDetailsViewDao extends JpaRepository<CollectionPaymentDetailsViewModel, Serializable>{

	
	@Query("select cpd from  CollectionPaymentDetailsViewModel cpd where cpd.companyId =:companyId and "
			+ "cpd.documentNo =:documentNo and cpd.documentFinancialYear=:documentFinancialYear "
			+ "and cpd.documentCode =:documentCode")
	public List<CollectionPaymentDetailsViewModel> getCollectedPaymentDetails(@Param("companyId") BigDecimal companyId,
			@Param("documentNo") BigDecimal documentNo,
			@Param("documentFinancialYear") BigDecimal documentFinancialYear,
			@Param("documentCode") BigDecimal documentCode);
}
