package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.forexoutlook.ForexOutlook;

public interface IForexOutlookDao extends JpaRepository<ForexOutlook, Serializable> {

	@Query("select fo from ForexOutlook fo where fo.pairId=:pairId "
			+ "and fo.langId=:langId and fo.isActive ='Y'")
	public List<ForexOutlook> getCurrencyPairById(@Param("pairId") BigDecimal pairId,
			@Param("langId") BigDecimal langId);
	
	@Query("Select fo from ForexOutlook fo where fo.isActive ='Y' ORDER BY fo.pairId")
	List<ForexOutlook> findAll();

}
