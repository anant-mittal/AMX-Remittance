package com.amx.jax.ui;

public enum ResponseMessage {

	USER_ALREADY_ACTIVE("User Already Active"), USER_ALREADY_LOGGIN("User already logged in"),

	AUTH_SUCCESS("User authenticated succfully"), AUTH_FAILED("User authenticated failed"),

	UNKNOWN_ERROR("error.unknown");

	private final String message;

	ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
