package com.amx.jax.userservice.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;

@Component
public class CustomerPersonalDetailValidator implements Validator {

	@Autowired
	IServiceApplicabilityRuleDao serviceApplicabilityRuleDao;

	@Autowired
	MetaData metaData;

	@Autowired
	TenantContext<CustomerValidation> tenantContext;

	@Autowired
	ValidationClients validationClients;

	@Override
	public boolean supports(Class clazz) {
		return CustomerPersonalDetailValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		CustomerRegistrationTrnxModel beneficiaryTrnxModel = (CustomerRegistrationTrnxModel) target;
		CustomerPersonalDetail customerPersonalDetail = beneficiaryTrnxModel.getCustomerPersonalDetail();
		tenantContext.get().validateCivilId(customerPersonalDetail.getIdentityInt());
		validateMobileNumberLength(customerPersonalDetail.getCountryId(), customerPersonalDetail.getMobile());
	}

	protected void validateMobileNumberLength(BigDecimal countryId, String mobile) {

		ValidationClient validationClient = validationClients.getValidationClient(countryId.toString());
		if (!validationClient.isValidMobileNumber(mobile)) {
			throw new GlobalException("Mobile Number length is not correct.", JaxError.INCORRECT_LENGTH);
		}
	}

}
