package com.amx.jax.radar.snap;

public enum SnapQueryTemplate {
	CUSTOMER_LIMIT("customer-limit"),
	CUSTOMERS_JOINED("customer-joined"),
	CUSTOMERS_JOINED_ONLINE("customer-joined-online"),

	;

	String file;

	SnapQueryTemplate(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}
}
