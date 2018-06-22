package com.amx.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.exception.AmxApiError;

public class InvalidCivilIdException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCivilIdException(String errorMessage) {
		super(errorMessage);
	}
	
	public InvalidCivilIdException(AmxApiError error) {
		super(error);
	}


}
