package com.amx.jax.exception;

public abstract class AbstractException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	public AbstractException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public abstract String getErrorCode();

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
