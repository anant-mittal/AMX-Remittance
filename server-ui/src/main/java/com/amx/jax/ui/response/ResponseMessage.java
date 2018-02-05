package com.amx.jax.ui.response;

public enum ResponseMessage {

	USER_ALREADY_ACTIVE("User Already Active"), USER_ALREADY_LOGGIN("User already logged in"),

	AUTH_SUCCESS("User authentication succfully"), AUTH_FAILED("User authentication failed"), UNAUTHORIZED(
			"UnAuthorized access or logged out"),

	UNKNOWN_ERROR("error.unknown");

	private final String message;

	ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
