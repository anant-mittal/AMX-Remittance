package com.amx.jax.logger.events;

import java.math.BigDecimal;

public class CustInfo {
	BigDecimal id;
	String identity;

	public CustInfo() {
		super();
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

}
