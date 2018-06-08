package com.amx.amxlib.exception;

import com.amx.jax.exception.AmxApiError;

public class InvalidJsonInputException extends AbstractJaxException {
	
	public InvalidJsonInputException(AmxApiError error) {
		super(error);
	}

	public InvalidJsonInputException(String errorMessage) {
		super(errorMessage);
	}

	private static final long serialVersionUID = 1L;
}
