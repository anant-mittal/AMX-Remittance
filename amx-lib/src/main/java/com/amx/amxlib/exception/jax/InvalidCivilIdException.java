package com.amx.amxlib.exception.jax;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.exception.AmxApiError;

public class InvalidCivilIdException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCivilIdException(String errorMessage) {
		super(errorMessage, JaxError.JAX_FIELD_VALIDATION_FAILURE.getCode());
	}
	
	public InvalidCivilIdException(AmxApiError error) {
		super(error);
	}


}
