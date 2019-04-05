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
import com.amx.jax.error.JaxCustomerError;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserValidationService;

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
		if (customerOnlineRegistration != null && customerOnlineRegistration.getDeviceId() != null
				&& customerOnlineRegistration.getDevicePassword() != null) {
			customerFlags.setFingerprintlinked(Boolean.TRUE);
		} else {
			customerFlags.setFingerprintlinked(Boolean.FALSE);
		}
			
		customerFlags.setFingerprintlinked(Boolean.TRUE);
		
		Customer customer = custDao.getCustById(customerOnlineRegistration.getCustomerId());
		Date annualIncomeUpdateDate = customer.getAnnualIncomeUpdatedDate();
		if (annualIncomeUpdateDate == null) {
			customerFlags.setAnnualIncomeExpired(Boolean.TRUE);
			logger.debug("Flag value is " + customerFlags.getAnnualIncomeExpired());
			
		}
		else {
			Date currentDate = new Date();
			long millisec = currentDate.getTime() - annualIncomeUpdateDate.getTime();
			long milliSecInYear = 31540000000L;

			if (millisec >= milliSecInYear) {
				customerFlags.setAnnualIncomeExpired(Boolean.TRUE);
				logger.debug("Flag value isss " + customerFlags.getAnnualIncomeExpired());
		} else {
				customerFlags.setAnnualIncomeExpired(Boolean.FALSE);
				logger.debug("Flag value isssss " + customerFlags.getAnnualIncomeExpired());
			}
		}
		return customerFlags;
	}
	
	public void validateInformationOnlyCustomer(BigDecimal customerId) {
		CustomerFlags customerFlags = getCustomerFlags(customerId);
		if (!Boolean.TRUE.equals(customerFlags.getSecurityQuestionRequired())) {
			throw new GlobalException(JaxCustomerError.SECURITY_QUESTION_REQUIRED, "Security question required");
		}
		if (!Boolean.TRUE.equals(customerFlags.getSecurityAnswerRequired())) {
			throw new GlobalException(JaxCustomerError.SECURITY_ANSWER_REQUIRED, "Security answer required");
		}
	}
}
