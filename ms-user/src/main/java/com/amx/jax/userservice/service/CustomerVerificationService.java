package com.amx.jax.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.CustomerVerificationType;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.userservice.repository.CustomerVerificationRepository;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerVerificationService {

	@Autowired
	CustomerVerificationRepository customerVerificationRepository;

	public void saveOrUpdateVerification(CustomerVerification customerVerification) {
		customerVerificationRepository.save(customerVerification);
	}

	public void updateVerification(Customer cust, CustomerVerificationType type, String value) {
		CustomerVerification customerverification = customerVerificationRepository
				.findBycustomerIdAndVerificationType(cust.getCustomerId(), type.toString());
		if (customerverification != null) {
			customerverification.setFieldValue(value);
		}
		customerVerificationRepository.save(customerverification);
	}
	
	public CustomerVerification getVerification(Customer cust, CustomerVerificationType type, String value) {
		return customerVerificationRepository
				.findBycustomerIdAndVerificationType(cust.getCustomerId(), type.toString());
		
	}

}
