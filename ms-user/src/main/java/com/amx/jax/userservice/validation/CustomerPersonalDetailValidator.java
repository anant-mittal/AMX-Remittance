package com.amx.jax.userservice.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.validation.CountryMetaValidation;

@Component
public class CustomerPersonalDetailValidator implements Validator {

	@Autowired
	IServiceApplicabilityRuleDao serviceApplicabilityRuleDao;
	@Autowired
	MetaData metaData;
	@Autowired
	TenantContext<CustomerValidation> tenantContext;
	@Autowired
	CountryMetaValidation countryMetaValidation;
	@Autowired
	CustomerDao custDao;
	@Autowired
	UserValidationService userValidationService;

	@Override
	public boolean supports(Class clazz) {
		return CustomerPersonalDetailValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		CustomerRegistrationTrnxModel beneficiaryTrnxModel = (CustomerRegistrationTrnxModel) target;
		CustomerPersonalDetail customerPersonalDetail = beneficiaryTrnxModel.getCustomerPersonalDetail();
		tenantContext.get().validateCivilId(customerPersonalDetail.getIdentityInt());
		countryMetaValidation.validateMobileNumberLength(customerPersonalDetail.getCountryId(),
				customerPersonalDetail.getMobile());
		userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(customerPersonalDetail.getIdentityInt(),
				JaxApiFlow.SIGNUP_DEFAULT);
	}

}
