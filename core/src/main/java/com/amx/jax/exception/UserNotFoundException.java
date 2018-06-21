package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;

public class UserNotFoundException extends AbstractJaxException {

	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(AmxApiError error) {
		super(error);
	}
	
	@Override
	public String getErrorCode() {
		return JaxError.USER_NOT_FOUND.getCode();
	}

}
