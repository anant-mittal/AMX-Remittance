package com.amx.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.exception.AmxApiError;

public class UserNotFoundException extends AbstractJaxException {

	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
	public UserNotFoundException(AmxApiError error) {
		super(error);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
