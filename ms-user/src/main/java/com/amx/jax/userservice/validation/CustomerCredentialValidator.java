package com.amx.jax.userservice.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerCredential;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.service.UserValidationService;

@Component
public class CustomerCredentialValidator implements Validator {

	@Autowired
	UserValidationService userValidation;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerCredentialValidator.class);

	@Override
	public boolean supports(Class clazz) {
		return CustomerCredentialValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		CustomerRegistrationTrnxModel customerRegistrationTrnxModel = (CustomerRegistrationTrnxModel) target;
		CustomerCredential customerCredential = customerRegistrationTrnxModel.getCustomerCredential();
		String identityInt = customerRegistrationTrnxModel.getCustomerPersonalDetail().getIdentityInt();
		String loginId = customerCredential.getLoginId();
		/*if (!identityInt.equals(loginId)) {
			throw new GlobalException("Login id should be same as identity number entered", JaxError.INVALID_INPUT);
		}*/
		userValidation.validateAllLoginId(loginId);
		userValidation.validateNonActiveOrNonRegisteredCustomerStatus(identityInt, JaxApiFlow.SIGNUP_DEFAULT);
	}

}
