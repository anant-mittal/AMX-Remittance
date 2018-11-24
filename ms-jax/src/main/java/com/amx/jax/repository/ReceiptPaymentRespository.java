package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ReceiptPayment;

public interface ReceiptPaymentRespository extends CrudRepository<ReceiptPayment, BigDecimal>{

}
