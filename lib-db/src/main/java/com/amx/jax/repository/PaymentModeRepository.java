package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.PaymentModeModel;


public interface PaymentModeRepository  extends CrudRepository<PaymentModeModel, Serializable>{

	@Query("select c from PaymentModeModel c where paymentModeCode=:paymentModeCode and  isActive='Y'")
	public PaymentModeModel getPaymentModeDetails(@Param("paymentModeCode") String paymentModeCode);
}
