package com.amx.jax.exception;

import com.amx.amxlib.error.JaxError;

public class InvalidOtpException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOtpException(String errorMessage) {
		super(errorMessage, JaxError.INVALID_OTP.getCode());
	}
	
	public InvalidOtpException(AmxApiError error) {
		super(error);
	}

	@Override
	public String getErrorCode() {
		return JaxError.INVALID_OTP.getCode();
	}

}
