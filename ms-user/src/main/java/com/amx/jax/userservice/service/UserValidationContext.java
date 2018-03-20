package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.userservice.service.UserValidationContext.UserValidation;

import com.amx.jax.scope.TenantContext;

@Service
public class UserValidationContext extends TenantContext<UserValidation> {

	@Autowired
	public UserValidationContext(List<UserValidation> libs) {
		super(libs);
	}

	public interface UserValidation {
		public void validateCustIdProofs(BigDecimal custId);
	}

}
