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

	/**
	 * Stores
	 * 
	 * @param employeeNumber
	 * @param userOtpData
	 * @return
	 */
	public boolean cacheUserOtpData(String employeeNumber, UserOtpData userOtpData) {
		if (null != userOtpData && null != userOtpData.getOtpData()) {
			userOtpData.getOtpData().setmOtp("");
			userOtpData.getOtpData().seteOtp("");

			if (null != userOtpData.getPartnerOtpData()) {
				userOtpData.getPartnerOtpData().setmOtp("");
				userOtpData.getPartnerOtpData().seteOtp("");
			}
		}
		return super.fastPut(employeeNumber, userOtpData);
	}

	@Override
	@Deprecated
	public boolean fastPut(String employeeNumber, UserOtpData userOtpData) {
		return super.fastPut(employeeNumber, userOtpData);
	}

}
