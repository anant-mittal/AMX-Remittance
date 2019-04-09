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
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.AmxDBConstants;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerFlagManager {

	private static final Logger logger = Logger.getLogger(CustomerFlags.class);

	@Autowired
	UserValidationService userValidationService;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	CustomerDao custDao;

	public CustomerFlags getCustomerFlags(BigDecimal customerId) {

		CustomerFlags customerFlags = new CustomerFlags();
		Customer customer = custDao.getCustById(customerId);

		try {
			userValidationService.validateCustIdProofs(customerId);
		} catch (GlobalException ex) {
			customerFlags.setIdProofStatus(ex.getErrorKey());
		}
		customerIdProofManager.setIdProofFlags(customerId, customerFlags);

		CustomerOnlineRegistration customerOnlineRegistration = custDao.getOnlineCustByCustomerId(customerId);
		customerFlags.setFingerprintlinked(isFingerprintLinked(customerOnlineRegistration));
		customerFlags.setSecurityQuestionRequired(isSecurityQuestionRequired(customerOnlineRegistration));

		customerFlags.setAnnualIncomeExpired(isAnnualIncomeExpired(customer));
		setCustomerCommunicationChannelFlags(customer, customerFlags);

		return customerFlags;
	}

	private static boolean isSecurityQuestionRequired(CustomerOnlineRegistration customerOnlineRegistration) {
		if (customerOnlineRegistration == null) {
			return true;
		}
		if (customerOnlineRegistration.getSecurityQuestion1() == null) {
			return true;
		}
		if (customerOnlineRegistration.getSecurityQuestion2() == null) {
			return true;
		}
		if (customerOnlineRegistration.getSecurityQuestion3() == null) {
			return true;
		}
		if (customerOnlineRegistration.getSecurityQuestion4() == null) {
			return true;
		}
		if (customerOnlineRegistration.getSecurityQuestion5() == null) {
			return true;
		}
		return false;
	}

	private void setCustomerCommunicationChannelFlags(Customer customer, CustomerFlags customerFlags) {
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
	}

	public void validateInformationOnlyCustomer(BigDecimal customerId) {
		CustomerFlags customerFlags = getCustomerFlags(customerId);
		if (!Boolean.TRUE.equals(customerFlags.getSecurityQuestionRequired())) {
			throw new GlobalException(JaxError.SQA_SETUP_REQUIRED, "Security question required");
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
