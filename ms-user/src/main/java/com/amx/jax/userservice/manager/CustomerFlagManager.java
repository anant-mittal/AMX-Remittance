package com.amx.jax.userservice.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.error.JaxCustomerError;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserValidationService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerFlagManager {

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

		CustomerOnlineRegistration customerOnlineRegistration = userValidationService
				.validateOnlineCustomerByIdentityId(customerId);

		if (customerOnlineRegistration == null || customerOnlineRegistration.getDeviceId() == null
				|| customerOnlineRegistration.getDevicePassword() == null) {
			customerFlags.setFingerprintlinked(Boolean.FALSE);
		} else {
			customerFlags.setFingerprintlinked(Boolean.TRUE);
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
