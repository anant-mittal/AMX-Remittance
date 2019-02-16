package com.amx.jax.radar.snap;

public enum SnapQueryTemplate {
	CUSTOMER_LIMIT("customer-limit", "oracle-v3-*-v4"),
	CUSTOMERS_JOINED("customer-joined", "oracle-v3-*-v4"),
	CUSTOMERS_PROFILE("customer-profile", "oracle-v3-customer-v4"),
	TRANX_DONE("tranx-done", "oracle-v3-*-v4"),
	XRATE_SELL_TRANSFER("xrate-sell-transfer", "marketrate"),
	;

	String file;
	String index;

	SnapQueryTemplate(String file) {
		this.file = file;
	}

	SnapQueryTemplate(String file, String index) {
		this.file = file;
		this.index = index;
	}

	public String getFile() {
		return file;
	}

	public String getIndex() {
		return index;
	}
}
