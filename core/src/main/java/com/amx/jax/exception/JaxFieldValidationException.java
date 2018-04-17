package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;

public class JaxFieldValidationException extends AbstractAppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JaxFieldValidationException(String errorMessage) {
		super(errorMessage, JaxError.JAX_FIELD_VALIDATION_FAILURE.getCode());
	}

}
