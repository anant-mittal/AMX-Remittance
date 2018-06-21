package com.amx.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.exception.AmxApiError;

public class InvalidJsonInputException extends AbstractJaxException {

	public InvalidJsonInputException(String errorMessage) {
		super(errorMessage);
	}
	
	public InvalidJsonInputException(AmxApiError error) {
		super(error);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


}
