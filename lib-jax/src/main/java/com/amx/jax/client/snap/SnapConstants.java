package com.amx.jax.client.snap;

import com.amx.jax.def.AbstractQueryFactory.IQueryTemplate;

public class SnapConstants {

	public static final String ORACLE = "oracle";
	public static final String DOC_VERSION = "v4";

	public static class SnapIndexName {

		public static final String TRANX = "trnx";
		public static final String CUSTOMER = "customer";
		public static final String XRATE = "xrate";
		public static final String VERIFY = "verifiy";
		public static final String LOGS = "logs";
		public static final String ALL = "*";

	}

	public static final String esindex(String prefix) {
		return String.format("%s-%s-%s-*", ORACLE, DOC_VERSION, prefix);
	}

	public static enum SnapQueryTemplate implements IQueryTemplate {
		FIND_DOC_BY_ID("find-doc-by-id", SnapIndexName.ALL),
		CUSTOMER_LIMIT("customer-limit", SnapIndexName.ALL),
		CUSTOMERS_JOINED("customer-joined", SnapIndexName.CUSTOMER),
		CUSTOMERS_PROFILE("customer-profile", SnapIndexName.CUSTOMER),
		TRANX_DONE("tranx-done", SnapIndexName.TRANX),
		TRANX_ANOMALY("tranx-anomaly", SnapIndexName.TRANX),
		XRATE_SELL_TRANSFER("xrate-sell-transfer", SnapIndexName.XRATE),
		CUSTOMER_VERIFICATION_REPORT("customer-verification-report", SnapIndexName.LOGS, "auditlogs"),
		CUSTOMER_VERIFICATION_REPORT_TOTAL("customer-verification-report-total", SnapIndexName.LOGS, "auditlogs"),
		BUGZ_STORIES("bugz-stories", SnapIndexName.ALL),

		RPT("rpt", SnapIndexName.TRANX),
		RPT2("rpt2", SnapIndexName.TRANX),
		
		ACTIVE_DEVICE_REPORT("active-device", SnapIndexName.ALL),
		ACTIVE_SIGNPAD_REPORT("active-signpad", SnapIndexName.ALL),
		ACTIVE_TERMINAL_REPORT("active-terminal", SnapIndexName.ALL);

		String file;
		String index;
		String indexType;
		String queryParams;

		public String getQueryParams() {
			return queryParams;
		}

		public void setQueryParams(String queryParams) {
			this.queryParams = queryParams;
		}

		SnapQueryTemplate(String file) {
			this.file = file;
		}

		SnapQueryTemplate(String file, String indexType) {
			this.file = file;
			this.indexType = indexType;
			this.index = esindex(indexType);
		}

		SnapQueryTemplate(String file, String indexType, String index) {
			this.file = file;
			this.indexType = indexType;
			this.index = index;
		}

		public String getFile() {
			return file;
		}

		public String getIndex() {
			return index;
		}

		public String getIndexType() {
			return indexType;
		}

		public void setIndexType(String indexType) {
			this.indexType = indexType;
		}

		static {
			RPT.setQueryParams("q:val");
		}

	}
}
