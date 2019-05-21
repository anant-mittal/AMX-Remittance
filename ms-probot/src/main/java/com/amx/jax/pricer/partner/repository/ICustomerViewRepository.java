package com.amx.jax.pricer.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.partner.dbmodel.CustomerDetailsView;

public interface ICustomerViewRepository extends CrudRepository<CustomerDetailsView, Serializable> {
	
	public CustomerDetailsView findByCustomerId(BigDecimal customerId);

}
