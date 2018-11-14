package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.scope.TenantContext;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;

@Service
public class CustomerValidationContext extends TenantContext<CustomerValidation> {

	@Autowired
	public CustomerValidationContext(List<CustomerValidation> libs) {
		super(libs);
	}

	public interface CustomerValidation {
		public void validateCustIdProofs(BigDecimal custId);
		
		public void validateCivilId(String civilId);
		
		public void validateEmailId(String emailId);
		
		public void validateDuplicateMobile(String mobileNo);
		
	}

}
