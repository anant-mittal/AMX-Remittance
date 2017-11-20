package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.repository.CustomerRepository;

@Component
public class CustomerDao {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private MetaData meta;

	public Customer getCustomerByCivilId(String civilId) {
		Customer cust = null;
		BigDecimal countryId = new BigDecimal(meta.getCountryId());
		List<Customer> customers = repo.getCustomer(countryId, civilId);
		if (customers != null && !customers.isEmpty()) {
			cust = customers.get(0);
		}
		return cust;
	}

}
