package com.amx.jax.radar.jobs.customer;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

/**
 * The Class LoggedInUsers.
 */
@Component
public class OracleVarsCache extends CacheBox<String> {

	public static final String DOC_VERSION = "v2";

	/**
	 * Instantiates a new logged in users.
	 */
	public OracleVarsCache() {
		super("OracleVarsCache");
	}

	public String getTranxIndex() {
		return "oracle-" + DOC_VERSION + "-tranx-v2";
	}

	public String getCustomerIndex() {
		return "oracle-" + DOC_VERSION + "-customer-v2";
	}

	public Long getCustomerScannedStamp() {
		return ArgUtil.parseAsLong(this.get(getCustomerIndex()), 0L);
	}

	public void setCustomerScannedStamp(Long customerScannedStamp) {
		this.put(getCustomerIndex(), ArgUtil.parseAsString(customerScannedStamp));
	}

	public Long getTranxScannedStamp() {
		return ArgUtil.parseAsLong(this.get(getTranxIndex()), 0L);
	}

	public void setTranxScannedStamp(Long tranxScannedStamp) {
		this.put(getTranxIndex(), ArgUtil.parseAsString(tranxScannedStamp));
	}

}
