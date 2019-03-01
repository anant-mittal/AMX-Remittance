package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CurrencyPairView;
import com.amx.jax.dbmodel.forexoutlook.ForexOutlook;

public interface ICurrencyPairRepository extends JpaRepository<CurrencyPairView, BigDecimal> {
	@Query("Select c from CurrencyPairView c ORDER BY c.pairId")
	List<CurrencyPairView> findAll();
	
	@Query("select c from CurrencyPairView c where c.pairId=:pairId ")
	public CurrencyPairView getCurrencyPairById(@Param("pairId") BigDecimal pairId);

}
