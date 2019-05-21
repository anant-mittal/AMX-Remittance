package com.amx.jax.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.partner.dbmodel.CustomerDetailsView;

public interface ICustomerViewRepository extends CrudRepository<CustomerDetailsView, Serializable> {
	
	public CustomerDetailsView findByCustomerId(BigDecimal customerId);

}
