package com.amx.jax.customer.manager;

import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.request.CustomerPersonalDetail;

@Component
public class CustomerPersonalDetailManager {

	public void updateCustomerPersonalDetail(Customer customer, CustomerPersonalDetail customerPersonalDetail) {
		validateRequest(customer, customerPersonalDetail);
	}

	private void validateRequest(Customer customer, CustomerPersonalDetail customerPersonalDetail) {

	}
}
