/**
 * 
 */
package com.amx.jax.customer.manager;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.LocalAddressDetails;
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

	/**
	 * @param customers
	 *            validate cust for reg in ofsite
	 */
	public void validateOffsiteCustomerForRegistration(List<Customer> customers) {
		Customer customer = null;
		if (customers != null && customers.size() > 0) {
			List<Customer> activeCustomers = customers.stream()
					.filter(i -> ConstantDocument.Yes.equals(i.getIsActive())).collect(Collectors.toList());
			if (activeCustomers.size() > 1) {
				throw new GlobalException(JaxError.DUPLICATE_CUSTOMER_NOT_ACTIVE_BRANCH,
						"Duplicate Customer not active in branch, please visit branch");
			}
			if (CollectionUtils.isNotEmpty(activeCustomers)) {
				customer = activeCustomers.get(0);
				Date now = new Date();
				if (customer.getIdentityExpiredDate().compareTo(now) > 0) {
					throw new GlobalException(JaxError.CUSTOMER_ACTIVE_BRANCH, "Customer already active in branch");
				}
			}
		}
	}

	public void validateLocalContact(LocalAddressDetails localAddressDetails) {
		if (StringUtils.isEmpty(localAddressDetails.getHouse())) {
			throw new GlobalException("House cannot be empty");
		}
		if (StringUtils.isEmpty(localAddressDetails.getFlat())) {
			throw new GlobalException("Flat cannot be empty");
		}
		if (StringUtils.isEmpty(localAddressDetails.getBlock())) {
			throw new GlobalException("Block cannot be empty");
		}
		if (StringUtils.isEmpty(localAddressDetails.getStreet())) {
			throw new GlobalException("Street cannot be empty");
		}
		if (localAddressDetails.getCountryId() == null) {
			throw new GlobalException("Country cannot be empty");
		}
	}

	public void validateHomeContact(HomeAddressDetails homeAddressDetails) {
		if (homeAddressDetails.getCountryId() == null) {
			throw new GlobalException("Country cannot be empty");
		}
	}
}
