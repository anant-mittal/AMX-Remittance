/**
 * 
 */
package com.amx.jax.customer.manager;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.error.JaxError;
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
		List<Customer> customers = userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(request.getIdentityInt(),
				request.getIdentityType(), JaxApiFlow.OFFSITE_REGISTRATION);
		if (CollectionUtils.isNotEmpty(customers) && ConstantDocument.Yes.equals(customers.get(0).getIsActive())) {
			userValidationService.validateCustIdProofs(customers.get(0).getCustomerId());
		}
	}

	/**
	 * @param customers
	 * @return active customer. If more than one record found with status 'N' then last updated customer is returned
	 * If more than 1 actrive found then error is thrown
	 */
	public Customer validateOffsiteCustomerForRegistration(List<Customer> customers) {
		Customer customer = null;
		if (customers != null && customers.size() > 0) {
			List<Customer> activeCustomers = customers.stream()
					.filter(i -> ConstantDocument.Yes.equals(i.getIsActive())).collect(Collectors.toList());
			if (activeCustomers.size() > 1) {
				throw new GlobalException(JaxError.DUPLICATE_CUSTOMER_NOT_ACTIVE_BRANCH,
						"Duplicate Customer not active in branch, please visit branch");
			}
			if (CollectionUtils.isNotEmpty(activeCustomers)) {
				return activeCustomers.get(0);
			}
			customers.sort((c1, c2) -> {
				return c1.getLastUpdated().compareTo(c2.getLastUpdated());
			});
			customer = customers.get(0);
		}
		return customer;
	}
}
