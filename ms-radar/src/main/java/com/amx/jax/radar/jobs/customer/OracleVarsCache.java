package com.amx.jax.radar.jobs.customer;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

/**
 * The Class LoggedInUsers.
 */
@Component
public class OracleVarsCache extends CacheBox<String> {

	/**
	 * Instantiates a new logged in users.
	 */
	public OracleVarsCache() {
		super("OracleVarsCache");
	}

	public Long getCustomerScannedStamp() {
		return ArgUtil.parseAsLong(this.get("getCustomerScannedStamp"));
	}

	public void setCustomerScannedStamp(Long customerScannedStamp) {
		this.put("getCustomerScannedStamp", ArgUtil.parseAsString(customerScannedStamp));
	}

}
