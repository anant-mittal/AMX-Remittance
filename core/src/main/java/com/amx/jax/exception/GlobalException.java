package com.amx.jax.exception;

public class GlobalException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GlobalException(String errorMessage) {
		super(errorMessage);
	}

	public GlobalException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}
}
