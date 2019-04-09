package com.amx.jax.userservice.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
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

		CustomerOnlineRegistration customerOnlineRegistration = custDao.getOnlineCustByCustomerId(customerId);
		if (customerOnlineRegistration != null && customerOnlineRegistration.getDeviceId() != null
				&& customerOnlineRegistration.getDevicePassword() != null) {
			customerFlags.setFingerprintlinked(Boolean.TRUE);
		} else {
			customerFlags.setFingerprintlinked(Boolean.FALSE);
		}
		return customerFlags;
	}
}
