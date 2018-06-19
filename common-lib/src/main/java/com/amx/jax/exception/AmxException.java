package com.amx.jax.exception;

public class AmxException extends Exception {

	private static final long serialVersionUID = 7427401687837732495L;

	protected String errorMessage;

	protected String errorCode;

	public AmxException(Exception e) {
		super(e);
	}

	public AmxException(String msg) {
		super(msg);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
