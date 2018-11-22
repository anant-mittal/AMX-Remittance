package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.placeorder.PlaceOrderCustomer;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.pricer.repository.CustomerRepository;
import com.google.common.collect.Lists;

@Component
public class CustomerDao {

	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private MetaData meta;
	

	@Transactional
	public Customer getCustomerByCivilId(String civilId) {
		Customer cust = null;
		BigDecimal countryId = meta.getCountryId();
		List<Customer> customers = customerRepo.getCustomerbyuser(countryId, civilId);
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
	
	public List<PlaceOrderCustomer> getPersonInfoById(List<BigDecimal> customerIds) {
		List<PlaceOrderCustomer> poCustomers = new ArrayList<>();
		List<List<BigDecimal>> partitions = Lists.partition(customerIds, 999);
		for (List<BigDecimal> partition : partitions) {
			poCustomers.addAll(customerRepo.findPOCustomersByIds(partition));
		}

		return poCustomers;
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
