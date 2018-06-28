package com.amx.jax.exception;

import com.amx.utils.ArgUtil;

public class AmxException extends RuntimeException {

	private static final long serialVersionUID = 7427401687837732495L;

	protected String errorMessage;

	protected String errorCode;

	public AmxException(Exception e) {
		super(e);
	}

	public AmxException(String msg) {
		super(msg);
	}

	public AmxException(String errorCode, String errorMessage) {
		super(String.format("[%s] - %s", errorCode, errorMessage));
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	public AmxException(int errorCode, String statusText) {
		this(ArgUtil.parseAsString(errorCode), statusText);
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
