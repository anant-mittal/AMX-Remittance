package com.amx.jax.radar.snap;

public enum SnapQueryTemplate {
	CUSTOMER_LIMIT("customer-limit");

	String file;

	SnapQueryTemplate(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}
}
