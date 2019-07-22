package com.amx.jax.branch.beans;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.utils.ArgUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BranchSession {

	@Autowired
	CustomerContext customerRequest;

	@Autowired
	CustomerContext customerSession;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	public CustomerContext getCustomerContext(boolean session) {
		if (session) {
			return customerSession;
		} else {
			return customerRequest;
		}
	}

	public CustomerContext getCustomerContext() {
		if (!ArgUtil.isEmpty(customerRequest.getCustomer()) || !ArgUtil.isEmpty(commonHttpRequest.get("identity"))) {
			return customerRequest;
		}
		return customerSession;
	}

	public BigDecimal getCustomerId() {
		return getCustomerContext().getCustomerId();
	}

	public void setCustomer(OffsiteCustomerDataDTO customer) {
		getCustomerContext().setCustomer(customer);
	}

	public OffsiteCustomerDataDTO getCustomer() {
		return getCustomerContext().getCustomer();
	}

}
