package com.amx.jax.ui.response;

/**
 * The Enum ResponseMessage.
 */
public enum ResponseMessage {

	/** The user already active. */
	USER_ALREADY_ACTIVE("User Already Active"),
	/** The user already loggin. */
	USER_ALREADY_LOGGIN("User already logged in"),

	/** The auth success. */
	AUTH_SUCCESS("User authentication succfully"),
	/** The auth failed. */
	AUTH_FAILED("User authentication failed"),
	/** The unauthorized. */
	UNAUTHORIZED("UnAuthorized access or logged out"),

	/** The unknown error. */
	UNKNOWN_ERROR("error.unknown");

	/** The message. */
	private final String message;

	/**
	 * Instantiates a new response message.
	 *
	 * @param message
	 *            the message
	 */
	ResponseMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
