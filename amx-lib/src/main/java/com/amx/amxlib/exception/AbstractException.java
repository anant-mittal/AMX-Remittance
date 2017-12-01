package com.amx.amxlib.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.response.ApiError;

public abstract class AbstractException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	protected JaxError error;

	public AbstractException(ApiError error) {
		this.error = JaxError.valueOf(error.getErrorId());
		this.errorMessage = error.getErrorMessage();
	}

	public AbstractException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public AbstractException(String errorMessage, String errorCode) {
		super();
		this.errorMessage = errorMessage;
		this.error = JaxError.valueOf(errorCode);
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public JaxError getError() {
		return error;
	}

	public void setError(JaxError error) {
		this.error = error;
	}
}
