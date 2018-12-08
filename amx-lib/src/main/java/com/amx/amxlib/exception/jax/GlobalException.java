package com.amx.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.ExceptionMessageKey;

public class GlobalException extends AbstractJaxException {

	private static final long serialVersionUID = 1L;

	public GlobalException(String errorMessage) {
		super(errorMessage, JaxError.JAX_FIELD_VALIDATION_FAILURE.getStatusKey());
	}

	public GlobalException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}

	public GlobalException(JaxError error, String errorMessage) {
		super(error, errorMessage);
	}

	public GlobalException(AmxApiError error) {
		super(error);
	}

	public GlobalException(JaxError error, Object... expressions) {
		this.error = error;
		this.errorKey = ExceptionMessageKey.build(error, expressions);
	}

	public GlobalException(String errorMessage, JaxError error, Object... expressions) {
		this.error = error;
		this.errorKey = ExceptionMessageKey.build(error, expressions);
		this.errorMessage = errorMessage;
	}

}
