package com.amx.amxlib.exception;

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
