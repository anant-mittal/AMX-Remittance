package com.amx.jax.userservice.validation;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.OtpData;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.DateUtil;
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
	@Autowired
	OtpSettings otpSettings;
	
	@Autowired
	CustomerRegistrationManager customerRegistrationManager;
	
	@Autowired
	DateUtil dateUtil;

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
		countryMetaValidation.validateMobileNumber(customerPersonalDetail.getCountryId(), customerPersonalDetail.getMobile());
		countryMetaValidation.validateMobileNumberLength(customerPersonalDetail.getCountryId(), customerPersonalDetail.getMobile());
		userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(customerPersonalDetail.getIdentityInt(), JaxApiFlow.SIGNUP_DEFAULT);
		validateCustomerBlackList(customerPersonalDetail);
		OtpData otpData = customerRegistrationManager.get().getOtpData();
		resetAttempts(otpData);
		customerRegistrationManager.saveOtpData(otpData);
		validateOtpSendCount(beneficiaryTrnxModel.getOtpData());
	}

	private void validateOtpSendCount(OtpData otpData) {
		if (otpData.getSendOtpAttempts() >= otpSettings.getMaxSendOtpAttempts()) {
			throw new GlobalException(JaxError.VALIDATE_OTP_LIMIT_EXCEEDED,
					"Sorry, you cannot proceed to register. Please try to register after 12 midnight");
		}
	}

	private void validateCustomerBlackList(CustomerPersonalDetail customerPersonalDetail) {
		StringBuilder customerName = new StringBuilder();
		if (StringUtils.isNotBlank(customerPersonalDetail.getFirstName())) {
			customerName.append(customerPersonalDetail.getFirstName().trim().toUpperCase());
		}
		if (StringUtils.isNotBlank(customerPersonalDetail.getLastName())) {
			customerName.append(customerPersonalDetail.getLastName().trim().toUpperCase());
		}
		List<BlackListModel> blist = blackListDao.getBlackByName(customerName.toString());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Customer is black listed", JaxError.BLACK_LISTED_CUSTOMER.getStatusKey());
		}
	}
	
	/** resets attempts of otp */
	private void resetAttempts(OtpData otpData) {
		Date midnightToday = dateUtil.getMidnightToday();

		if (otpData.getLockDate() != null && midnightToday.compareTo(otpData.getLockDate()) > 0) {
			otpData.resetCounts();
		}
	}
}
