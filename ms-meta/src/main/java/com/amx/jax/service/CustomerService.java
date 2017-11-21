package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.Customer;
import com.amx.jax.repository.ICustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	ICustomerRepository customerRepository;
	
	public List<Customer> getCustomer(BigDecimal countryId,String userId){
		return customerRepository.getCustomer(countryId, userId);
	}
	
	/*@Transactional
	public  ResponseEntity<List<Customer>> convert(BigDecimal countryId,String userId){
		List<Customer> customerDetailList = customerRepository.getCustomer(countryId, userId);
		for(Customer c: customerDetailList) {
		Hibernate.initialize(c);
		}
		return new ResponseEntity<List<Customer>>(customerDetailList, HttpStatus.OK);
	}*/

}
