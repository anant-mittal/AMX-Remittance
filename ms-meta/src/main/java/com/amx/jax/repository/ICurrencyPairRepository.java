package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.amx.jax.dbmodel.CurrencyPairView;

public interface ICurrencyPairRepository extends JpaRepository<CurrencyPairView, BigDecimal> {
	@Query("Select c from CurrencyPairView c ORDER BY c.pairId")
	List<CurrencyPairView> findAll();

}
