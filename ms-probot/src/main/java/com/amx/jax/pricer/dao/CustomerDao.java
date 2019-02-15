package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.repository.CustomerRepository;

@Component
public class CustomerDao {

	@Autowired
	private CustomerRepository customerRepo;
		

	@Transactional
	public Customer getCustomerByCivilId(String civilId) {
		Customer cust = null;
		List<Customer> customers = customerRepo.getCustomerByIdentityInt(civilId);
		if (customers != null && !customers.isEmpty()) {
			cust = customers.get(0);
		}
		return cust;
	}
	
	public List<Customer> findActiveCustomers(String identityInt) {
		return customerRepo.findActiveCustomers(identityInt);
	}
	
	@Transactional
	public List<Customer> getCustomerByIdentityInt(String identityInt) {
		return customerRepo.getCustomerByIdentityInt(identityInt);
	}
	

	@Transactional
	public Customer getCustById(BigDecimal id) {
		return customerRepo.findOne(id);
	}
	
	public Customer getCustomerByCountryAndUserId(BigDecimal countryId,String userId) {
		List<Customer> list = customerRepo.getCustomerbyuser(countryId, userId);
		Customer customer = null;
		if (list != null) {
			customer = list.get(0);
		}
		return customer;
	}
	
	public Customer getCustomerByMobile(String mobile) {
		List<Customer> list = customerRepo.getCustomerByMobile(mobile);
		Customer customer = null;
		if (list != null && list.size()!=0) {
			customer = list.get(0);
		}
		return customer;
	}
	
	public void saveCustomer(Customer c) {
		customerRepo.save(c);
	}
	
	
	
}
