package com.amx.jax.ui;

public enum ResponseMessage {

	USER_ALREADY_ACTIVE(ResponseStatus.ALREADY_ACTIVE, "error.usr.active.alrdy"),

	INVALID_ID(ResponseStatus.INVALID_ID, "error.invalid.id"),

	OTP_SENT(ResponseStatus.OTP_SENT, "msg.otp.sent"),

	UNKNOWN_ERROR(ResponseStatus.ERROR, "error.unknown");

	private final ResponseStatus status;
	private final String message;

	ResponseMessage(ResponseStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
