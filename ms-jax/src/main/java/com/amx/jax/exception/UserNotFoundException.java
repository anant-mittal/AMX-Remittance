package com.amx.jax.exception;

public class UserNotFoundException extends AbstractException {

	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorCode() {
		return "USER_NOT_FOUND";
	}

}
