package com.amx.jax.rbaac.dto;

/**
 * Stores offline otp data which will be sent to client
 * 
 * @author prashant
 *
 */
public class OfflineOtpDto {

	/**
	 * prefix generated while sending otp
	 */
	String prefix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
