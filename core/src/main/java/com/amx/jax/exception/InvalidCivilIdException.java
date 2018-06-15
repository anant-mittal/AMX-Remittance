package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;

public class InvalidCivilIdException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCivilIdException(String errorMessage) {
		super(errorMessage);
	}
	
	public InvalidCivilIdException(AmxApiError error) {
		super(error.getErrorClass());
	}

	@Override
	public String getErrorCode() {
		return JaxError.INVALID_CIVIL_ID.getCode();
	}

}
