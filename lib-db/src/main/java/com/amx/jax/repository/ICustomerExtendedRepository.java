package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerExtendedModel;

public interface ICustomerExtendedRepository  extends CrudRepository<CustomerExtendedModel, Serializable>{

	public CustomerExtendedModel findByCustomerId(BigDecimal customerId);
}
