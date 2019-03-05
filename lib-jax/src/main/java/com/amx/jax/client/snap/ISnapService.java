package com.amx.jax.client.snap;

import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.dict.Currency;

public interface ISnapService {

	public static class Path {

		public static final String SNAP_API_XRATE_SELL_TRANSFER = "/snap/api/xrate/sell_transfer";

		public static final String SNAP_API_TRANX_DONE = "/snap/api/tranx/done";

		public static final String SNAP_API_CUSTOMER_JOINED = "/snap/api/customer/joined";

	}

	public static enum RateType {
		BUY_CASH("BC"), SELL_CASH("SC"), SELL_TRNSFR("ST");
		String code;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		RateType(String code) {
			this.code = code;
		}
	}

	public static enum RateSource {
		AMANKUWAIT, BECKWT, MUZAINI, UAEXCHANGE, AMX
	}

	public static enum StatsSpan {
		WEEK1, MONTH1, YEAR1, YEAR2, YEAR5
	}

	public SnapModelWrapper getCustomerStats();

	public SnapModelWrapper getXRateStats(StatsSpan graph, Currency forCur, Currency domCur);

	public SnapModelWrapper getTranxStats();
}
