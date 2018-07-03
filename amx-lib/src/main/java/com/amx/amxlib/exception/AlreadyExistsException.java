package com.amx.amxlib.exception;

import com.amx.jax.exception.AmxApiError;

public class AlreadyExistsException extends AbstractJaxException {

	private static final long serialVersionUID = 1L;

	public AlreadyExistsException(AmxApiError error) {
		super(error);
	}

}
