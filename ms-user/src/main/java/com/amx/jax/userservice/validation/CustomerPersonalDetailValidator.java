package com.amx.jax.userservice.validation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BlackListModel;
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
	@Autowired
	BlackListDao blackListDao;

	@Override
	public boolean supports(Class clazz) {
		return CustomerPersonalDetailValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		CustomerRegistrationTrnxModel beneficiaryTrnxModel = (CustomerRegistrationTrnxModel) target;
		CustomerPersonalDetail customerPersonalDetail = beneficiaryTrnxModel.getCustomerPersonalDetail();
		tenantContext.get().validateCivilId(customerPersonalDetail.getIdentityInt());
		tenantContext.get().validateEmailId(customerPersonalDetail.getEmail());
		countryMetaValidation.validateMobileNumber(customerPersonalDetail.getCountryId(),
				customerPersonalDetail.getMobile());
		countryMetaValidation.validateMobileNumberLength(customerPersonalDetail.getCountryId(),
				customerPersonalDetail.getMobile());
		userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(customerPersonalDetail.getIdentityInt(),
				JaxApiFlow.SIGNUP_DEFAULT);
		validateCustomerBlackList(customerPersonalDetail);
	}

	private void validateCustomerBlackList(CustomerPersonalDetail customerPersonalDetail)
	{
		StringBuilder customerName = new StringBuilder();
		if(StringUtils.isNotBlank(customerPersonalDetail.getFirstName())) {
			customerName.append(customerPersonalDetail.getFirstName().trim().toUpperCase());
		}		
		if(StringUtils.isNotBlank(customerPersonalDetail.getLastName())) {
			customerName.append(customerPersonalDetail.getLastName().trim().toUpperCase());
		}	
		List<BlackListModel> blist =blackListDao.getBlackByName(customerName.toString());		
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Customer is black listed",
					JaxError.BLACK_LISTED_CUSTOMER.getCode());
		}
	}
}
