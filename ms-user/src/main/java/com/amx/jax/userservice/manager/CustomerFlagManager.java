package com.amx.jax.userservice.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.error.JaxError;
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
		if(customerOnlineRegistration == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}
		if(customerOnlineRegistration.getDeviceId()== null || customerOnlineRegistration.getDevicePassword()==null) {
			customerFlags.setFingerprintlinked(Boolean.FALSE);
			return customerFlags;
		}
			
		customerFlags.setFingerprintlinked(Boolean.TRUE);
		return customerFlags;
	}
}
