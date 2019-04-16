package com.amx.jax.repository;

/**
 * @auth: rabil
 * @date :24 Mar 2019
 */
import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerCoreDetailsView;

public interface CustomerCoreDetailsRepository extends CrudRepository<CustomerCoreDetailsView, Serializable>{
	
	public CustomerCoreDetailsView findByCustomerID(BigDecimal customerId);

}
