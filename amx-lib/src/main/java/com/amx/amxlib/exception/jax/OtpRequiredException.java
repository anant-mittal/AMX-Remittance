package com.amx.amxlib.exception.jax;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;

public class OtpRequiredException extends AbstractJaxException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OtpRequiredException() {
		// TODO Auto-generated constructor stub
	}
	
	public OtpRequiredException(String errorMessage) {
		super(JaxError.JAX_FIELD_VALIDATION_FAILURE.getStatusKey(), errorMessage);
	}
	
	public OtpRequiredException(AmxApiError error) {
		super(error);
	}

}
