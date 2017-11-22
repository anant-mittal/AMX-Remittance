package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.repository.ICustomerIdProofDAO;

@Service
public class CustomerIdProofService {
	
	
	@Autowired
	ICustomerIdProofDAO customerIdProofRepository;
	
	
	public List<CustomerIdProof> getCustomerIdProofByCustomerId(BigDecimal customerId){
		return customerIdProofRepository.getCustomerIdProofByCustomerId(customerId);
	}

}
