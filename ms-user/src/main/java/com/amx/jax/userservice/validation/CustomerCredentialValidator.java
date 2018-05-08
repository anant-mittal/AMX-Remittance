package com.amx.jax.userservice.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.amxlib.model.CustomerCredential;
import com.amx.jax.userservice.service.UserValidationService;

@Component
public class CustomerCredentialValidator implements Validator {

	@Autowired
	UserValidationService userValidation;

	@Override
	public boolean supports(Class clazz) {
		return CustomerCredentialValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		CustomerCredential customerCredential = (CustomerCredential) target;
		String loginId = customerCredential.getLoginId();
		userValidation.validateAllLoginId(loginId);

	}

}
