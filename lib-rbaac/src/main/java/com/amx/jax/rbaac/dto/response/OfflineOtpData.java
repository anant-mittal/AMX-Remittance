package com.amx.jax.rbaac.dto.response;

import java.io.Serializable;

/**
 * Offline otp data
 * 
 * @author prashant
 *
 */
public class OfflineOtpData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String otpPrefix;

	public OfflineOtpData(String otpPrefix) {
		super();
		this.otpPrefix = otpPrefix;
	}

	public OfflineOtpData() {
		super();
	}

	public String getOtpPrefix() {
		return otpPrefix;
	}

	public void setOtpPrefix(String otpPrefix) {
		this.otpPrefix = otpPrefix;
	}
}
