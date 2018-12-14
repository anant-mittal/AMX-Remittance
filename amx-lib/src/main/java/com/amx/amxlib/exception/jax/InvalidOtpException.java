package com.amx.amxlib.exception.jax;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;

public class InvalidOtpException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOtpException(String errorMessage) {
		super(JaxError.INVALID_OTP, errorMessage);
	}
	
	public InvalidOtpException(AmxApiError error) {
		super(error);
	}

}
