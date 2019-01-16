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
	private static final String ASC_SEPERATOR = "-";
	private static final String DESC_SEPERATOR = "-desc-";
	private static final String CUSTOMER_RESET_COUNTER = "9";
	private static final String TRANSACTION_RESET_COUNTER = "9";
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
		return ArgUtil.parseAsLong(this.get(getCustomerIndex() + ASC_SEPERATOR + CUSTOMER_RESET_COUNTER), START_TIME);
	}

	public void setCustomerScannedStamp(Long customerScannedStamp) {
		this.put(getCustomerIndex() + ASC_SEPERATOR + CUSTOMER_RESET_COUNTER,
				ArgUtil.parseAsString(customerScannedStamp));
	}

	public Long getTranxScannedStamp(boolean reverse) {
		if (reverse) {
			return ArgUtil.parseAsLong(this.get(getTranxIndex() + DESC_SEPERATOR + TRANSACTION_RESET_COUNTER),
					System.currentTimeMillis());
		}
		return ArgUtil.parseAsLong(this.get(getTranxIndex() + ASC_SEPERATOR + TRANSACTION_RESET_COUNTER), START_TIME);
	}

	public Long getTranxScannedStamp() {
		return this.getTranxScannedStamp(false);
	}

	public void setTranxScannedStamp(Long tranxScannedStamp, boolean reverse) {
		if (reverse) {
			this.put(getTranxIndex() + DESC_SEPERATOR + TRANSACTION_RESET_COUNTER,
					ArgUtil.parseAsString(tranxScannedStamp));
		}
		this.put(getTranxIndex() + ASC_SEPERATOR + TRANSACTION_RESET_COUNTER, ArgUtil.parseAsString(tranxScannedStamp));
	}

	public void setTranxScannedStamp(Long tranxScannedStamp) {
		this.setTranxScannedStamp(tranxScannedStamp, false);
	}

}
