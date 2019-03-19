package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.error.JaxError;
import com.amx.jax.userservice.dao.CustomerDao;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OffsiteCustomerRegManager {

	@Autowired
	CustomerDao customerDao;
	@Autowired
	OffsiteCustomerRegValidator offsiteCustomerRegValidator;

	/**
	 * Returns the customer for offiste registration. Only inactive /not registered/
	 * active customers whose id is expired
	 * 
	 * @param identityInt
	 * @param identityType
	 * @return
	 */
	public Customer getCustomerForRegistration(String identityInt, BigDecimal identityType) {
		Customer customer = null;
		String[] status = { ConstantDocument.Yes, ConstantDocument.No };
		List<Customer> customers = customerDao.getActiveCustomerByIndentityIntAndTypeAndIsActive(identityInt,
				identityType, Arrays.asList(status));
		offsiteCustomerRegValidator.validateOffsiteCustomerForRegistration(customers);
		if (CollectionUtils.isNotEmpty(customers)) {
			Date now = new Date();
			customer = customers.stream().filter(i -> {
				Date idExpiryDate = i.getIdentityExpiredDate();
				boolean isActive = ConstantDocument.Yes.equals(i.getIsActive());
				boolean isIdExpired = idExpiryDate != null && idExpiryDate.compareTo(now) > 0;
				return isActive && isIdExpired;
			}).findFirst().get();
			if (customer == null) {
				customer = customers.get(0);
			}
		}
		return customer;
	}

}
