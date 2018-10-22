package com.amx.jax.userservice.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.CustomerCredential;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.service.UserValidationService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerCredentialValidator implements Validator {

	@Autowired
	UserValidationService userValidation;
	
	Boolean isPartialReg;
	
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
		/*
		 * if (!identityInt.equals(loginId)) { throw new
		 * GlobalException("Login id should be same as identity number entered",
		 * JaxError.INVALID_INPUT); }
		 */
		userValidation.validateAllLoginId(loginId);
		if (isPartialReg != null && isPartialReg) {
			userValidation.validateNonActiveOrNonRegisteredCustomerStatus(identityInt, JaxApiFlow.SIGNUP_DEFAULT);
		}
	}

	public Boolean getIsPartialReg() {
		return isPartialReg;
	}

	public void setIsPartialReg(Boolean isPartialReg) {
		this.isPartialReg = isPartialReg;
	}

}
