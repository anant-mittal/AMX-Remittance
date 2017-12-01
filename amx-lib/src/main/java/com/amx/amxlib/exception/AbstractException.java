package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public abstract class AbstractException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	protected String errorCode;

	public AbstractException(ApiError error) {
		this.errorCode = error.getErrorId();
		this.errorMessage = error.getErrorMessage();
	}

	public AbstractException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public AbstractException(String errorMessage, String errorCode) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
