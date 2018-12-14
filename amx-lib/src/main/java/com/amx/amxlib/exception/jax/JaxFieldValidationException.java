package com.amx.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;

public class JaxFieldValidationException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JaxFieldValidationException(AmxApiError error) {
		super(error);
	}

	public JaxFieldValidationException(String errorMessage) {
		super(JaxError.JAX_FIELD_VALIDATION_FAILURE.getStatusKey(), errorMessage);
	}

}
