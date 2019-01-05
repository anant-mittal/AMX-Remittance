package com.amx.jax.repository;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.ForeignCurrencyStockTransfer;

public interface ForeignCurrencyStockRepository extends CrudRepository<ForeignCurrencyStockTransfer, String>{

}
