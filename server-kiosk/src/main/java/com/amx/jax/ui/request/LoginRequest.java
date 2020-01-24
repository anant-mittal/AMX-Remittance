package com.amx.jax.ui.request;

/**
 * The Class LoginRequest.
 */
public class LoginRequest {

	/** The identity. */
	private String identity;

	/** The password. */
	private String password;

	/** The otp. */
	private String otp;

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Sets the identity.
	 *
	 * @param identity
	 *            the new identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the otp.
	 *
	 * @return the otp
	 */
	public String getOtp() {
		return otp;
	}

	/**
	 * Sets the otp.
	 *
	 * @param otp
	 *            the new otp
	 */
	public void setOtp(String otp) {
		this.otp = otp;
	}
}
