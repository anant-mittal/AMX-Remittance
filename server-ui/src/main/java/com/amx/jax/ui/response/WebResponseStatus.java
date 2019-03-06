package com.amx.jax.ui.response;

import com.amx.jax.exception.IExceptionEnum;

/**
 * The Enum WebResponseStatus.
 */
public enum WebResponseStatus implements IExceptionEnum {

	UI_SERVER_ERROR("000"),
	/** The already active. */
	// Registration - CIVIL ID validation
	ALREADY_ACTIVE("302"),
	DEVICE_LOCKED("302"),
	/** The invalid id. */
	INVALID_ID("200"),
	/** The otp sent. */
	OTP_SENT("200"),

	/** The active session. */
	// Registration - OTP validation
	ACTIVE_SESSION("302"),
	/** The verify success. */
	VERIFY_SUCCESS("200"),
	/** The verify failed. */
	VERIFY_FAILED("401"),

	/** The user update init. */
	// User Updates
	USER_UPDATE_INIT("200"),
	/** The user update success. */
	USER_UPDATE_SUCCESS("200"),
	/** The logout done. */
	LOGOUT_DONE("200"),
	/** The user update failed. */
	USER_UPDATE_FAILED("300"),

	/** The auth failed. */
	// Auth Reponses
	AUTH_FAILED("302"),
	/** The auth ok. */
	AUTH_OK("200"),
	/** The auth done. */
	AUTH_DONE("200"),
	/** The auth blocked temp. */
	AUTH_BLOCKED_TEMP("200"),
	/** The auth blocked. */
	AUTH_BLOCKED("200"),
	/** The unauthorized. */
	UNAUTHORIZED("401"),

	/** The dotp required. */
	// Info Required
	DOTP_REQUIRED("300"),
	MOTP_REQUIRED("300"),
	OTP_REQUIRED("300"),

	/** The unknown jax error. */
	UNKNOWN_JAX_ERROR("500"),

	/** The bad input. */
	// Registration - END ERROR
	BAD_INPUT("400"),
	/** The success. */
	SUCCESS("200"),
	/** The error. */
	ERROR("500"),

	/** The redirection. */
	REDIRECTION("3xx"),
	/** The server error. */
	SERVER_ERROR("4xx"),
	/** The client error. */
	CLIENT_ERROR("5xx");

	/** The code. */
	private final String code;

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Instantiates a new web response status.
	 *
	 * @param code the code
	 */
	WebResponseStatus(String code) {
		this.code = code;
	}

	@Override
	public String getStatusKey() {
		return this.toString();
	}

	@Override
	public int getStatusCode() {
		return this.ordinal();
	}

}