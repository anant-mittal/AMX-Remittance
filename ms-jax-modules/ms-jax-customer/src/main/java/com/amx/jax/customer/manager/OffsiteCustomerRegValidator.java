/**
 * 
 */
package com.amx.jax.customer.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.model.request.customer.GetOffsiteCustomerDetailRequest;
import com.amx.jax.userservice.service.UserValidationService;

/**
 * @author Prashant
 *
 */
@Component
public class OffsiteCustomerRegValidator {

	@Autowired
	UserValidationService userValidationService;

	public void validateGetOffsiteCustomerDetailRequest(GetOffsiteCustomerDetailRequest request) {
		userValidationService.validateIdentityInt(request.getIdentityInt(), request.getIdentityType());
		// to validate duplicate account
		userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(request.getIdentityInt(),
				request.getIdentityType(), JaxApiFlow.OFFSITE_REGISTRATION);
	}
}
