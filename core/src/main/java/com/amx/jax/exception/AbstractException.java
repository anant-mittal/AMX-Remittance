package com.amx.jax.exception;

import com.amx.amxlib.model.response.JaxFieldError;

public abstract class AbstractException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	protected String errorCode;
	
	private JaxFieldError validationErrorField;

	public AbstractException() {
		super();
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

	public JaxFieldError getValidationErrorField() {
		return validationErrorField;
	}

	public void setValidationErrorField(JaxFieldError validationErrorField) {
		this.validationErrorField = validationErrorField;
	}
}
