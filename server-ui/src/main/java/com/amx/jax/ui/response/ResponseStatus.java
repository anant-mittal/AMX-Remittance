package com.amx.jax.ui.response;

public enum ResponseStatus {

	// Registration - CIVIL ID validation
	ALREADY_ACTIVE("302"), INVALID_ID("200"), OTP_SENT("200"),

	// Registration - OTP validation
	ACTIVE_SESSION("302"), VERIFY_SUCCESS("200"), VERIFY_FAILED("401"),

	// User Updates
	USER_UPDATE_SUCCESS("200"), LOGOUT_DONE("200"), USER_UPDATE_FAILED("300"),

	// Auth Reponses
	AUTH_FAILED("302"), AUTH_OK("200"), AUTH_DONE("200"), AUTH_BLOCKED_TEMP("200"), AUTH_BLOCKED("200"),

	// Registration - END ERROR
	SUCCESS("200"), ERROR("500");

	private final String code;

	public String getCode() {
		return code;
	}

	ResponseStatus(String code) {
		this.code = code;
	}

}