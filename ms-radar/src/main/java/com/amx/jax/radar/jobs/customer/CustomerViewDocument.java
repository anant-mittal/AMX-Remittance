package com.amx.jax.radar.jobs.customer;

import java.util.Map;

import com.amx.jax.radar.AESDocument;

public class CustomerViewDocument extends AESDocument {

	Map<String, Object> customer;

	public Map<String, Object> getCustomer() {
		return customer;
	}

	public void setCustomer(Map<String, Object> customer) {
		this.customer = customer;
	}
}
