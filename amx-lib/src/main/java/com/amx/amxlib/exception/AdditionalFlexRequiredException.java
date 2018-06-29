package com.amx.amxlib.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.response.JaxTransactionResponse;
import com.amx.jax.exception.AmxApiError;

public class AdditionalFlexRequiredException extends AbstractJaxException {

	public AdditionalFlexRequiredException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdditionalFlexRequiredException(AmxApiError error) {
		super(error);
		// TODO Auto-generated constructor stub
	}
	
	public AdditionalFlexRequiredException(String errorMessage, JaxError error) {
		super(errorMessage, error.getCode());
	}

}
