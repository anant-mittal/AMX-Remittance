package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CollectionDetailViewModel;

public interface ICollectionDetailViewDao extends JpaRepository<CollectionDetailViewModel, Serializable>{

	
	@Query("select cdv from CollectionDetailViewModel cdv where cdv.companyId =:companyId and  cdv.documentNo =:documentNo "
			+ "and cdv.documentFinancialYear=:documentFinancialYear and cdv.documentCode=:documentCode")
	List<CollectionDetailViewModel>  getCollectionDetailView(
			@Param("companyId") BigDecimal companyId,
			@Param("documentNo") BigDecimal documentNo,
			@Param("documentFinancialYear") BigDecimal documentFinancialYear,
			@Param("documentCode") BigDecimal documentCode);
}
