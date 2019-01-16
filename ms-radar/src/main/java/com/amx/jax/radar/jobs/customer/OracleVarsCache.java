package com.amx.jax.radar.jobs.customer;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

/**
 * The Class LoggedInUsers.
 */
@Component
public class OracleVarsCache extends CacheBox<String> {

	public static final Long START_TIME = 978287400000L;
	private static final String STRING_DASH = "-";
	private static final String CUSTOMER_RESET_COUNTER = "8";
	private static final String TRANSACTION_RESET_COUNTER = "8";
	public static final String DOC_VERSION = "v3";

	/**
	 * Instantiates a new logged in users.
	 */
	public OracleVarsCache() {
		super("OracleVarsCache");
	}

	public String getTranxIndex() {
		return "oracle-" + DOC_VERSION + "-tranx-v4";
	}

	public String getCustomerIndex() {
		return "oracle-" + DOC_VERSION + "-customer-v4";
	}

	public Long getCustomerScannedStamp() {
		return ArgUtil.parseAsLong(this.get(getCustomerIndex() + STRING_DASH + CUSTOMER_RESET_COUNTER), START_TIME);
	}

	public void setCustomerScannedStamp(Long customerScannedStamp) {
		this.put(getCustomerIndex() + STRING_DASH + CUSTOMER_RESET_COUNTER,
				ArgUtil.parseAsString(customerScannedStamp));
	}

	public Long getTranxScannedStamp() {
		return ArgUtil.parseAsLong(this.get(getTranxIndex() + STRING_DASH + TRANSACTION_RESET_COUNTER), START_TIME);
	}

	public void setTranxScannedStamp(Long tranxScannedStamp) {
		this.put(getTranxIndex() + STRING_DASH + TRANSACTION_RESET_COUNTER, ArgUtil.parseAsString(tranxScannedStamp));
	}

}
