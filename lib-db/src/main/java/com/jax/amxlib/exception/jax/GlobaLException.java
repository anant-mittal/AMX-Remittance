package com.jax.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.ExceptionMessageKey;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.exception.IMessageKey;

@Deprecated
public class GlobaLException extends AbstractJaxException {

	private static final long serialVersionUID = 1L;

	public GlobaLException(String errorMessage) {
		super(JaxError.JAX_FIELD_VALIDATION_FAILURE.getStatusKey(), errorMessage);
	}

	public GlobaLException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public GlobaLException(JaxError error, String errorMessage) {
		super(error, errorMessage);
	}

	public GlobaLException(AmxApiError error) {
		super(error);
	}

	public GlobaLException(JaxError error, IMessageKey messageKey, String errorMessage) {
		this.error = error;
		this.errorKey = messageKey.toString();
		this.errorMessage = errorMessage;
	}

	public GlobaLException(JaxError error, IMessageKey messageKey) {
		this.error = error;
		this.errorKey = messageKey.toString();
	}

	@Deprecated
	public GlobaLException(JaxError error, Object... expressions) {
		this.error = error;
		this.errorKey = ExceptionMessageKey.build(error, expressions).toString();
	}

	@Deprecated
	public GlobaLException(String errorMessage, JaxError error, Object... expressions) {
		this.error = error;
		this.errorKey = ExceptionMessageKey.build(error, expressions).toString();
		this.errorMessage = errorMessage;
	}

	public GlobaLException(IExceptionEnum errorCode, String message) {
		super(errorCode, message);
	}
}
