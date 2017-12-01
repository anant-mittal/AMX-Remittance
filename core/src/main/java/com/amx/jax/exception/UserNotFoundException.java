package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.jax.exception.AbstractException;

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
		return JaxError.USER_NOT_FOUND.getCode();
	}

}
