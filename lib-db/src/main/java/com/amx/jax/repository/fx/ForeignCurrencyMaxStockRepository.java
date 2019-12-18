package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.ForeignCurrencyStockView;

public interface ForeignCurrencyMaxStockRepository extends CrudRepository<ForeignCurrencyStockView, Serializable>{
	
	public ForeignCurrencyStockView findByCurrencyId(BigDecimal currencyId);

}
