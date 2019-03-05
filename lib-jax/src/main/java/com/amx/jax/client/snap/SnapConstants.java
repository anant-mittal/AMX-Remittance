package com.amx.jax.client.snap;

public class SnapConstants {

	public static final String ORACLE = "oracle";
	public static final String DOC_VERSION = "v4";
	public static final String CUSTOMER = "customer";
	public static final String TRANX = "tranx";
	public static final String XRATE = "xrate";
	public static final String ALL = "*";

	public static final String esindex(String prefix) {
		return String.format("%s-%s-%s-*", ORACLE, DOC_VERSION, prefix);
	}

	public static enum SnapQueryTemplate {
		CUSTOMER_LIMIT("customer-limit", esindex(ALL)),
		CUSTOMERS_JOINED("customer-joined", esindex(CUSTOMER)),
		CUSTOMERS_PROFILE("customer-profile", esindex(CUSTOMER)),
		TRANX_DONE("tranx-done", esindex(TRANX)),
		XRATE_SELL_TRANSFER("xrate-sell-transfer", esindex(XRATE)),
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
}
