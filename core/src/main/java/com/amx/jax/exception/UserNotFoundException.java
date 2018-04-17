package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;

public class UserNotFoundException extends AbstractAppException {

	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorCode() {
		return JaxError.USER_NOT_FOUND.getCode();
	}

}
