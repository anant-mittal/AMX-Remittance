package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.PaygDetailsModel;

public interface PaygDetailsRepository extends CrudRepository<PaygDetailsModel, Serializable>{

	public PaygDetailsModel findByCollDocNumberAndCustomerId(BigDecimal collDocNumber,BigDecimal customerId);
	
	public PaygDetailsModel findByPgPaymentId(BigDecimal pgPaymentId);
}
