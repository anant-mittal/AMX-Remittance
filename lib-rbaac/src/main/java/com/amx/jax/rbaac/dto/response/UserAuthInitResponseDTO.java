/**
 * 
 */
package com.amx.jax.rbaac.dto.response;

import java.io.Serializable;

/**
 * The Class UserAuthInitResponseDTO.
 *
 * @author abhijeet
 */
public final class UserAuthInitResponseDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3674143419419981482L;

	/** The auth transaction id. */
	private String authTransactionId;

	/** The m otp prefix. */
	private String mOtpPrefix;

	/** The e otp prefix. */
	private String eOtpPrefix;

	/** The partner M otp prefix. */
	private String partnerMOtpPrefix;

	/** The partner E otp prefix. */
	private String partnerEOtpPrefix;

	/** The init otp time. */
	private String initOtpTime;

	/** The ttl otp. */
	private String ttlOtp;

	/**
	 * Gets the auth transaction id.
	 *
	 * @return the auth transaction id
	 */
	public String getAuthTransactionId() {
		return authTransactionId;
	}

	/**
	 * Sets the auth transaction id.
	 *
	 * @param authTransactionId
	 *            the new auth transaction id
	 */
	public void setAuthTransactionId(String authTransactionId) {
		this.authTransactionId = authTransactionId;
	}

	/**
	 * Gets the m otp prefix.
	 *
	 * @return the m otp prefix
	 */
	public String getmOtpPrefix() {
		return mOtpPrefix;
	}

	/**
	 * Sets the m otp prefix.
	 *
	 * @param mOtpPrefix
	 *            the new m otp prefix
	 */
	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	/**
	 * Gets the e otp prefix.
	 *
	 * @return the e otp prefix
	 */
	public String geteOtpPrefix() {
		return eOtpPrefix;
	}

	/**
	 * Sets the e otp prefix.
	 *
	 * @param eOtpPrefix
	 *            the new e otp prefix
	 */
	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

	/**
	 * Gets the partner M otp prefix.
	 *
	 * @return the partner M otp prefix
	 */
	public String getPartnerMOtpPrefix() {
		return partnerMOtpPrefix;
	}

	/**
	 * Sets the partner M otp prefix.
	 *
	 * @param partnerMOtpPrefix
	 *            the new partner M otp prefix
	 */
	public void setPartnerMOtpPrefix(String partnerMOtpPrefix) {
		this.partnerMOtpPrefix = partnerMOtpPrefix;
	}

	/**
	 * Gets the partner E otp prefix.
	 *
	 * @return the partner E otp prefix
	 */
	public String getPartnerEOtpPrefix() {
		return partnerEOtpPrefix;
	}

	/**
	 * Sets the partner E otp prefix.
	 *
	 * @param partnerEOtpPrefix
	 *            the new partner E otp prefix
	 */
	public void setPartnerEOtpPrefix(String partnerEOtpPrefix) {
		this.partnerEOtpPrefix = partnerEOtpPrefix;
	}

	/**
	 * Gets the inits the otp time.
	 *
	 * @return the inits the otp time
	 */
	public String getInitOtpTime() {
		return initOtpTime;
	}

	/**
	 * Sets the inits the otp time.
	 *
	 * @param initOtpTime
	 *            the new inits the otp time
	 */
	public void setInitOtpTime(String initOtpTime) {
		this.initOtpTime = initOtpTime;
	}

	/**
	 * Gets the ttl otp.
	 *
	 * @return the ttl otp
	 */
	public String getTtlOtp() {
		return ttlOtp;
	}

	/**
	 * Sets the ttl otp.
	 *
	 * @param ttlOtp
	 *            the new ttl otp
	 */
	public void setTtlOtp(String ttlOtp) {
		this.ttlOtp = ttlOtp;
	}

}
