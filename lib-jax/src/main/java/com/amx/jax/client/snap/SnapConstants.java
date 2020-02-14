package com.amx.jax.client.snap;

import com.amx.jax.def.AbstractQueryFactory.IQueryTemplate;
import com.amx.jax.grid.GridView;

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
		CUSTOMER_VERIFICATION_REPORT2("customer-verification-report2", SnapIndexName.LOGS, "auditlogs"),
		CUSTOMER_VERIFICATION_REPORT_TOTAL("customer-verification-report-total", SnapIndexName.LOGS, "auditlogs"),
		CUSTOMER_VERIFICATION_REPORT_DB("customer-verification-report-db", GridView.CUSTOMER_VERIFICATION_REPORT),
		CUSTOMER_LOGIN("customer-login", SnapIndexName.LOGS, "auditlogs"),

		
		BUGZ_STORIES("bugz-stories", SnapIndexName.ALL),
		BUGZ_STORIES_STATUS("bugz-stories-status", SnapIndexName.ALL, "bugzilla-bugs"),
		
		RPTPG2("rptpg2", SnapIndexName.TRANX),
		RPT_DUMMY("rpt_dummy", SnapIndexName.TRANX),
		RPT("rpt", SnapIndexName.TRANX),
		RPT2("rpt2", SnapIndexName.TRANX),
		RPTMONTHLY("rptmonthly", SnapIndexName.TRANX),
		RPTCOMPARISON("rptcomparison", SnapIndexName.TRANX),
		RPTMONTHCOMPARISON("rptmonthcomparison", SnapIndexName.TRANX),
		
		TRNX_LIFECYCLE("trnx-lifecycle", SnapIndexName.TRANX),
		TRNX_LIFECYCLE_DUMMY("trnx-lifecycle-dummy", SnapIndexName.TRANX),
		

		ACTIVE_DEVICE_REPORT("active-device", SnapIndexName.ALL),
		ACTIVE_SIGNPAD_REPORT("active-signpad", SnapIndexName.ALL),
		ACTIVE_TERMINAL_REPORT("active-terminal", SnapIndexName.ALL);

		String file;
		String index;
		String indexType;
		String queryParams;
		GridView gridView;

		public String getQueryParams() {
			return queryParams;
		}

		public void setQueryParams(String queryParams) {
			this.queryParams = queryParams;
		}

		SnapQueryTemplate(String file) {
			this.file = file;
		}

		SnapQueryTemplate(String file, GridView gridView) {
			this.file = file;
			this.gridView = gridView;
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
			RPT.setQueryParams("PrevMonth:PrevMonth;ThisMonth:ThisMonth");
			RPTPG2.setQueryParams("PrevMonth:PrevMonth;ThisMonth:ThisMonth");
			RPT_DUMMY.setQueryParams("PrevMonth:PrevMonth;ThisMonth:ThisMonth");
			RPTMONTHLY.setQueryParams("PrevMonth:PrevMonth;Month:Month");
			RPTCOMPARISON.setQueryParams("MonthOneFrom:2019-08-06;MonthOneTo:2019-09-06;MonthTwoFrom:2019-09-07;MonthTwoTo:2019-10-07");
			RPTMONTHCOMPARISON.setQueryParams("MonthOneName:Sept;MonthOne:2019-09;MonthTwoName:Oct;MonthTwo:2019-10");
			TRNX_LIFECYCLE.setQueryParams("Today:Today;Yesterday:Yesterday;BeforeYesterday:BeforeYesterday");
			TRNX_LIFECYCLE_DUMMY.setQueryParams("Today:Today;Yesterday:Yesterday;BeforeYesterday:BeforeYesterday");
			CUSTOMER_LOGIN.setQueryParams("traceid:D1F06B23-0001-4B62-BB2C-3D17F2F74964;logmap.client.fp:D1F06B23-0001-4B62-BB2C-3D17F2F74964;"
							+ "logmap.client.ip:188.236.139.6;customerId:513;logmap.agent.browser:CFNETWORK;logmap.agent.operatingSystem:MAC_OS_X;"
							+ "logmap.client.ct:ONLINE_WEB");
			CUSTOMER_VERIFICATION_REPORT_DB.setQueryParams("From:2020-02-01 00:00:00;To:2020-02-10 23:59:59");
		}

		public GridView getGridView() {
			return gridView;
		}

	}
}
