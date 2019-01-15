package com.amx.jax.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class OtpData.
 */
public class OtpData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m otp prefix. */
	private String mOtpPrefix;

	/** The e otp prefix. */
	private String eOtpPrefix;

	/** The m otp. */
	private String mOtp;

	/** The e otp. */
	private String eOtp;

	/** The hashedm otp. */
	private String hashedmOtp;

	/** The hashede otp. */
	private String hashedeOtp;

	/** The init time. */
	private long initTime;

	/** The ttl. */
	private long ttl;

	
	/** The send otp attempts. */
	// no of send otp attempts
	private int sendOtpAttempts;

	/** The validate otp attempts. */
	// no of send otp attempts
	private int validateOtpAttempts;

	/** The lock date. */
	// date when otp validation process is locked
	private Date lockDate;

	/** true if otp validation is successful. */
	private boolean otpValidated;

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
	 * Gets the m otp.
	 *
	 * @return the m otp
	 */
	public String getmOtp() {
		return mOtp;
	}

	/**
	 * Sets the m otp.
	 *
	 * @param mOtp
	 *            the new m otp
	 */
	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	/**
	 * Gets the e otp.
	 *
	 * @return the e otp
	 */
	public String geteOtp() {
		return eOtp;
	}

	/**
	 * Sets the e otp.
	 *
	 * @param eOtp
	 *            the new e otp
	 */
	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	/**
	 * Gets the hashedm otp.
	 *
	 * @return the hashedm otp
	 */
	public String getHashedmOtp() {
		return hashedmOtp;
	}

	/**
	 * Sets the hashedm otp.
	 *
	 * @param hashedmOtp
	 *            the new hashedm otp
	 */
	public void setHashedmOtp(String hashedmOtp) {
		this.hashedmOtp = hashedmOtp;
	}

	/**
	 * Gets the hashede otp.
	 *
	 * @return the hashede otp
	 */
	public String getHashedeOtp() {
		return hashedeOtp;
	}

	/**
	 * Sets the hashede otp.
	 *
	 * @param hashedeOtp
	 *            the new hashede otp
	 */
	public void setHashedeOtp(String hashedeOtp) {
		this.hashedeOtp = hashedeOtp;
	}

	/**
	 * Gets the inits the time.
	 *
	 * @return the inits the time
	 */
	public long getInitTime() {
		return initTime;
	}

	/**
	 * Sets the inits the time.
	 *
	 * @param initTime
	 *            the new inits the time
	 */
	public void setInitTime(long initTime) {
		this.initTime = initTime;
	}

	/**
	 * Gets the ttl.
	 *
	 * @return the ttl
	 */
	public long getTtl() {
		return ttl;
	}

	/**
	 * Sets the ttl.
	 *
	 * @param ttl
	 *            the new ttl
	 */
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OtpData [mOtpPrefix=" + mOtpPrefix + ", eOtpPrefix=" + eOtpPrefix + ", mOtp=" + mOtp + ", eOtp=" + eOtp
				+ ", hashedmOtp=" + hashedmOtp + ", hashedeOtp=" + hashedeOtp + ", initTime=" + initTime + ", ttl="
				+ ttl + ", sendOtpAttempts=" + sendOtpAttempts + ", validateOtpAttempts="
				+ validateOtpAttempts + ", lockDate=" + lockDate + ", otpValidated=" + otpValidated + "]";
	}

	/**
	 * Gets the lock date.
	 *
	 * @return the lock date
	 */
	public Date getLockDate() {
		return lockDate;
	}

	/**
	 * Sets the lock date.
	 *
	 * @param lockDate
	 *            the new lock date
	 */
	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	/**
	 * Checks if is otp validated.
	 *
	 * @return true, if is otp validated
	 */
	public boolean isOtpValidated() {
		return otpValidated;
	}

	/**
	 * Sets the otp validated.
	 *
	 * @param otpValidated
	 *            the new otp validated
	 */
	public void setOtpValidated(boolean otpValidated) {
		this.otpValidated = otpValidated;
	}

	/**
	 * Gets the send otp attempts.
	 *
	 * @return the send otp attempts
	 */
	public int getSendOtpAttempts() {
		return sendOtpAttempts;
	}

	/**
	 * Sets the send otp attempts.
	 *
	 * @param sendOtpAttempts
	 *            the new send otp attempts
	 */
	public void setSendOtpAttempts(int sendOtpAttempts) {
		this.sendOtpAttempts = sendOtpAttempts;
	}

	/**
	 * Gets the validate otp attempts.
	 *
	 * @return the validate otp attempts
	 */
	public int getValidateOtpAttempts() {
		return validateOtpAttempts;
	}

	/**
	 * Sets the validate otp attempts.
	 *
	 * @param validateOtpAttempts
	 *            the new validate otp attempts
	 */
	public void setValidateOtpAttempts(int validateOtpAttempts) {
		this.validateOtpAttempts = validateOtpAttempts;
	}

	/**
	 * Increment sent count.
	 */
	public void incrementSentCount() {
		this.sendOtpAttempts = this.sendOtpAttempts + 1;
	}

	/**
	 * Reset counts.
	 */
	public void resetCounts() {
		this.sendOtpAttempts = 0;
		this.validateOtpAttempts = 0;
		this.setLockDate(null);
	}
}
