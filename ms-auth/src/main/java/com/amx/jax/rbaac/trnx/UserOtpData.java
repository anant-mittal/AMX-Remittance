package com.amx.jax.rbaac.trnx;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

/**
 * The Class UserOtpData.
 */
@Component
public class UserOtpData extends CacheBox<String> {

	/**
	 * Instantiates a new logged in users.
	 */
	public UserOtpData() {
		super("Rbaac-UserOtpData");
	}

}
