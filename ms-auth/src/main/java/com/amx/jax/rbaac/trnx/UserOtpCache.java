package com.amx.jax.rbaac.trnx;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

/**
 * The Class UserOtpCache.
 */
@Component
public class UserOtpCache extends CacheBox<UserOtpData> {

	/**
	 * Instantiates a Cache for User OTP data.
	 */
	public UserOtpCache() {
		super("Rbaac-UserOtpCache");
	}

}
