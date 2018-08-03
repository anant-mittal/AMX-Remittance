package com.amx.amxlib.exception;

import com.amx.jax.exception.AmxApiError;

public class CustomerValidationException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerValidationException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}

	public CustomerValidationException(AmxApiError error) {
		super(error);
	}

}
