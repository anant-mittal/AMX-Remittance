package com.amx.amxlib.exception;

import com.amx.jax.exception.AmxApiError;

public class ResourceNotFoundException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(AmxApiError error) {
		super(error);
	}

}
