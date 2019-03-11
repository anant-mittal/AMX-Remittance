package com.amx.jax.client.snap;

public class SnapConstants {

	public static final String ORACLE = "oracle";
	public static final String DOC_VERSION = "v4";

	public static class SnapIndexName {

		public static final String TRANX = "trnx";
		public static final String CUSTOMER = "customer";
		public static final String XRATE = "xrate";
		public static final String ALL = "*";

	}

	public static final String esindex(String prefix) {
		return String.format("%s-%s-%s-*", ORACLE, DOC_VERSION, prefix);
	}

	public static enum SnapQueryTemplate {
		CUSTOMER_LIMIT("customer-limit", SnapIndexName.ALL),
		CUSTOMERS_JOINED("customer-joined", SnapIndexName.CUSTOMER),
		CUSTOMERS_PROFILE("customer-profile", SnapIndexName.CUSTOMER),
		TRANX_DONE("tranx-done", SnapIndexName.TRANX),
		XRATE_SELL_TRANSFER("xrate-sell-transfer", SnapIndexName.XRATE),
		;

		String file;
		String index;
		String indexName;

		SnapQueryTemplate(String file) {
			this.file = file;
		}

		SnapQueryTemplate(String file, String indexName) {
			this.file = file;
			this.indexName = indexName;
			this.index = esindex(indexName);
		}

		public String getFile() {
			return file;
		}

		public String getIndex() {
			return index;
		}

		public String getIndexName() {
			return indexName;
		}

		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}
	}
}
