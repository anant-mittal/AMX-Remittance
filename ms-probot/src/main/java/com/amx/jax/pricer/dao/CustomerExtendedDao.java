package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.CustomerExtended;
import com.amx.jax.pricer.repository.CustomerExtendedRepository;

@Component
public class CustomerExtendedDao {

	@Autowired
	CustomerExtendedRepository customerExtendedRepository;

	public CustomerExtended getCustomerExtendedByCustomerId(BigDecimal customerId) {
		return customerExtendedRepository.findByCustomerId(customerId);
	}
}
