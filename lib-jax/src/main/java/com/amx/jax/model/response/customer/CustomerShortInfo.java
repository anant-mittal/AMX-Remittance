package com.amx.jax.model.response.customer;

import java.math.BigDecimal;

public class CustomerShortInfo {

	BigDecimal customerId;

	private String identityInt;

	private BigDecimal identityTypeId;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
}
