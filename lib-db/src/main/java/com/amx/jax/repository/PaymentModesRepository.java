package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.PaymentModesModel;

public interface PaymentModesRepository extends CrudRepository<PaymentModesModel, Serializable>{
	@Query("select p from PaymentModesModel p order by p.paymentType")
	public List<PaymentModesModel> getPaymentModes();
}
