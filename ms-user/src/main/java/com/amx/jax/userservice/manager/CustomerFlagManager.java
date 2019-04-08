package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.postman.model.Message.Status;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.AmxDBConstants;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerFlagManager {
	Logger logger = Logger.getLogger(CustomerFlags.class);
	@Autowired
	UserValidationService userValidationService;

	@Autowired
	private CustomerDao custDao;

	public CustomerFlags getCustomerFlags(BigDecimal customerId) {

		CustomerFlags customerFlags = new CustomerFlags();

		try {
			userValidationService.validateCustIdProofs(customerId);
		} catch (GlobalException ex) {
			customerFlags.setIdProofStatus(ex.getErrorKey());
		}

		CustomerOnlineRegistration customerOnlineRegistration = custDao.getOnlineCustByCustomerId(customerId);
		customerFlags.setFingerprintlinked(isFingerprintLinked(customerOnlineRegistration));

		Customer customer = custDao.getCustById(customerOnlineRegistration.getCustomerId());
		customerFlags.setAnnualIncomeExpired(isAnnualIncomeExpired(customer));
		if (AmxDBConstants.Status.Y.equals(customer.getMobileVerified())) {
			customerFlags.setMobileVerified(Boolean.TRUE);
		} else {
			customerFlags.setMobileVerified(Boolean.FALSE);
		}
		if (AmxDBConstants.Status.Y.equals(customer.getWhatsAppVerified())) {
			customerFlags.setWhatsAppVerified(Boolean.TRUE);
		} else {
			customerFlags.setWhatsAppVerified(Boolean.FALSE);
		}
		if (AmxDBConstants.Status.Y.equals(customer.getEmailVerified())) {
			customerFlags.setEmailVerified(Boolean.TRUE);
		} else {
			customerFlags.setEmailVerified(Boolean.FALSE);
		}

		return customerFlags;
	}

	public void validateInformationOnlyCustomer(BigDecimal customerId) {
		CustomerFlags customerFlags = getCustomerFlags(customerId);
		if (!Boolean.TRUE.equals(customerFlags.getSecurityQuestionRequired())) {
			throw new GlobalException(JaxError.SQA_SETUP_REQUIRED, "Security question required");
		}
		if (!Boolean.TRUE.equals(customerFlags.getSecurityAnswerRequired())) {
			throw new GlobalException(JaxError.SQA_REQUIRED, "Security answer required");
		}
	}
	
	public static Boolean isFingerprintLinked(CustomerOnlineRegistration customerOnlineRegistration) {
		if (customerOnlineRegistration != null && customerOnlineRegistration.getDeviceId() != null
				&& customerOnlineRegistration.getDevicePassword() != null) {
			return true;
		} else {
			return false;
		}

	}
	
	public static Boolean isAnnualIncomeExpired(Customer customer) {
		Date annualIncomeUpdateDate = customer.getAnnualIncomeUpdatedDate();
		if (annualIncomeUpdateDate == null) {
			return true;
		} else {
			Date currentDate = new Date();
			long millisec = currentDate.getTime() - annualIncomeUpdateDate.getTime();
			long milliSecInYear = 31540000000L;

			if (millisec >= milliSecInYear) {
				return true;
			} else {
				return false;
			}
		}
	}
}
