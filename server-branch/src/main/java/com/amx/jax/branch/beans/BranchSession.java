package com.amx.jax.branch.beans;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BranchSession {

	private OffsiteCustomerDataDTO customer;

	public BigDecimal getCustomerId() {
		if (customer != null && customer.getCustomerPersonalDetail() != null) {
			return customer.getCustomerPersonalDetail().getCustomerId();
		}
		return null;
	}

	public void setCustomer(OffsiteCustomerDataDTO customer) {
		this.customer = customer;
	}

	public OffsiteCustomerDataDTO getCustomer() {
		return customer;
	}

}
