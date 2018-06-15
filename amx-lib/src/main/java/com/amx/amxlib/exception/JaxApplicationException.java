package com.amx.amxlib.exception;

import com.amx.jax.exception.AmxApiError;

public class JaxApplicationException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JaxApplicationException(AmxApiError error) {
		super(error);
	}

}
