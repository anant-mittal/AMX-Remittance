package com.amx.amxlib.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;

public abstract class AbstractJaxException extends AmxApiException {

	private static final long serialVersionUID = 1L;

	public AbstractJaxException(Exception e) {
		super(e);
	}

	public AbstractJaxException(AmxApiError error) {
		super(error);
	}

	public AbstractJaxException(String errorMessage) {
		super(errorMessage);
	}

	public AbstractJaxException(String errorMessage, String errorKey) {
		super(errorMessage, errorKey);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		return null;
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return JaxError.valueOf(errorId);
	}
}
