package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.FxOrderTranxLimitView;

public interface FxOrderTranxLimitRespository extends CrudRepository<FxOrderTranxLimitView, Serializable>{
	
	@Query("select x from FxOrderTranxLimitView x where x.customerId=?1")
	 public FxOrderTranxLimitView getFxTransactionLimit(BigDecimal customerId);
}
