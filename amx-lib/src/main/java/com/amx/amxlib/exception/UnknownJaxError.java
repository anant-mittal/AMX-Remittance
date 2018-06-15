package com.amx.amxlib.exception;

import com.amx.jax.exception.AmxApiError;

public class UnknownJaxError extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownJaxError(AmxApiError error) {
		super(error);
	}

}
