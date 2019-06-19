package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CustomerDetailsView;

public interface ICustomerViewRepository extends CrudRepository<CustomerDetailsView, Serializable> {
	
	public CustomerDetailsView findByCustomerId(BigDecimal customerId);

}
