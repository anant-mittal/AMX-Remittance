package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;

public class InvalidCivilIdException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCivilIdException(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public String getErrorCode() {
		return JaxError.INVALID_CIVIL_ID.getCode();
	}

}
