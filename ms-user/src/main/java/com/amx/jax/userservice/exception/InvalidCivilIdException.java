package com.amx.jax.userservice.exception;

import com.amx.jax.exception.AbstractException;

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

		return "INVALID_CIVIL_ID";
	}

}
