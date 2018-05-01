package com.amx.jax.trnx.model;

import java.io.Serializable;
import java.util.Date;

public class OtpData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mOtpPrefix;

	private String eOtpPrefix;

	private String mOtp;

	private String eOtp;

	private String hashedmOtp;

	private String hashedeOtp;

	// when otp was sent
	private Date sentDate;

	// no of send otp attempts
	private int sendOtpAttempts;

	// no of send otp attempts
	private int validateOtpAttempts;

	// date when otp validation process is locked
	private Date lockDate;

	/** true if otp validation is successful */
	private boolean otpValidated;

	public String getmOtpPrefix() {
		return mOtpPrefix;
	}

	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	public String geteOtpPrefix() {
		return eOtpPrefix;
	}

	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	public String geteOtp() {
		return eOtp;
	}

	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	public String getHashedmOtp() {
		return hashedmOtp;
	}

	public void setHashedmOtp(String hashedmOtp) {
		this.hashedmOtp = hashedmOtp;
	}

	public String getHashedeOtp() {
		return hashedeOtp;
	}

	public void setHashedeOtp(String hashedeOtp) {
		this.hashedeOtp = hashedeOtp;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	@Override
	public String toString() {
		return "OtpData [mOtpPrefix=" + mOtpPrefix + ", eOtpPrefix=" + eOtpPrefix + ", mOtp=" + mOtp + ", eOtp=" + eOtp
				+ ", hashedmOtp=" + hashedmOtp + ", hashedeOtp=" + hashedeOtp + ", sentDate=" + sentDate
				+ ", sendOtpAttempts=" + sendOtpAttempts + ", validateOtpAttempts=" + validateOtpAttempts + "]";
	}

	public Date getLockDate() {
		return lockDate;
	}

	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	public boolean isOtpValidated() {
		return otpValidated;
	}

	public void setOtpValidated(boolean otpValidated) {
		this.otpValidated = otpValidated;
	}

	public int getSendOtpAttempts() {
		return sendOtpAttempts;
	}

	public void setSendOtpAttempts(int sendOtpAttempts) {
		this.sendOtpAttempts = sendOtpAttempts;
	}

	public int getValidateOtpAttempts() {
		return validateOtpAttempts;
	}

	public void setValidateOtpAttempts(int validateOtpAttempts) {
		this.validateOtpAttempts = validateOtpAttempts;
	}

}
