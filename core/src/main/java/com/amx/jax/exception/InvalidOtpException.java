package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.jax.exception.AbstractException;

public class InvalidOtpException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOtpException(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public String getErrorCode() {
		return JaxError.INVALID_OTP.getCode();
	}

}
