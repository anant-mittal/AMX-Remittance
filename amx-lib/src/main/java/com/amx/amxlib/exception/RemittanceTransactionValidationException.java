package com.amx.amxlib.exception;

import java.util.List;

import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.jax.exception.AmxApiError;

public class RemittanceTransactionValidationException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemittanceTransactionValidationException(AmxApiError error) {
		super(error);
	}
}
