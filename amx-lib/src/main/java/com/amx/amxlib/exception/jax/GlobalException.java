package com.amx.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.ExceptionMessageKey;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.exception.IMessageKey;

public class GlobalException extends AbstractJaxException {

	private static final long serialVersionUID = 1L;

	public GlobalException(String errorMessage) {
		super(JaxError.JAX_FIELD_VALIDATION_FAILURE.getStatusKey(), errorMessage);
	}

	public GlobalException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public GlobalException(JaxError error, String errorMessage) {
		super(error, errorMessage);
	}

	public GlobalException(AmxApiError error) {
		super(error);
	}

	public GlobalException(JaxError error, IMessageKey messageKey, String errorMessage) {
		this.error = error;
		this.errorKey = messageKey.toString();
		this.errorMessage = errorMessage;
	}

	public GlobalException(JaxError error, IMessageKey messageKey) {
		this.error = error;
		this.errorKey = messageKey.toString();
	}

	@Deprecated
	public GlobalException(JaxError error, Object... expressions) {
		this.error = error;
		this.errorKey = ExceptionMessageKey.build(error, expressions).toString();
	}

	@Deprecated
	public GlobalException(String errorMessage, JaxError error, Object... expressions) {
		this.error = error;
		this.errorKey = ExceptionMessageKey.build(error, expressions).toString();
		this.errorMessage = errorMessage;
	}

	public GlobalException(IExceptionEnum errorCode, String message) {
		super(errorCode, message);
	}
}
