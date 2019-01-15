package com.amx.jax.radar.jobs.customer;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

/**
 * The Class LoggedInUsers.
 */
@Component
public class OracleVarsCache extends CacheBox<String> {

	private static final String CUSTOMER_RESET_COUNTER = "1";
	private static final String TRANSACTION_RESET_COUNTER = "2";
	public static final String DOC_VERSION = "v3";

	/**
	 * Instantiates a new logged in users.
	 */
	public OracleVarsCache() {
		super("OracleVarsCache");
	}

	public String getTranxIndex() {
		return "oracle-" + DOC_VERSION + "-tranx-v3";
	}

	public String getCustomerIndex() {
		return "oracle-" + DOC_VERSION + "-customer-v3";
	}

	public Long getCustomerScannedStamp() {
		return ArgUtil.parseAsLong(this.get(getCustomerIndex() + "-" + CUSTOMER_RESET_COUNTER), 0L);
	}

	public void setCustomerScannedStamp(Long customerScannedStamp) {
		this.put(getCustomerIndex() + "-" + CUSTOMER_RESET_COUNTER, ArgUtil.parseAsString(customerScannedStamp));
	}

	public Long getTranxScannedStamp() {
		return ArgUtil.parseAsLong(this.get(getTranxIndex() + "-" + TRANSACTION_RESET_COUNTER), 0L);
	}

	public void setTranxScannedStamp(Long tranxScannedStamp) {
		this.put(getTranxIndex() + "-" + TRANSACTION_RESET_COUNTER, ArgUtil.parseAsString(tranxScannedStamp));
	}

}
