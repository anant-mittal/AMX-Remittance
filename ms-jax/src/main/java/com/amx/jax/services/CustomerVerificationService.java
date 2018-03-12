package com.amx.jax.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.userservice.repository.CustomerVerificationRepository;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerVerificationService  {

	@Autowired
	CustomerVerificationRepository customerVerificationRepository ;
	
	public void saveOrUpdateVerification(CustomerVerification customerVerification) {
		customerVerificationRepository.save(customerVerification);
	}

}
