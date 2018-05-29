package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;

public class InvalidOtpException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOtpException(String errorMessage) {
		super(errorMessage, JaxError.INVALID_OTP.getCode());
	}

	@Override
	public String getErrorCode() {
		return JaxError.INVALID_OTP.getCode();
	}

}
