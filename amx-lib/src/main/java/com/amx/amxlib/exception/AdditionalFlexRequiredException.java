package com.amx.amxlib.exception;

import java.util.List;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.jax.exception.AmxApiError;

public class AdditionalFlexRequiredException extends AbstractJaxException {

	private static final long serialVersionUID = 3705286852744314145L;

	public AdditionalFlexRequiredException() {
		super();
	}

	public AdditionalFlexRequiredException(AmxApiError error) {
		super(error);
	}

	public AdditionalFlexRequiredException(String errorMessage, JaxError error) {
		super(errorMessage, error.getCode());
	}

	@SuppressWarnings("unchecked")
	public List<JaxConditionalFieldDto> getConditionalFileds() {
		return (List<JaxConditionalFieldDto>) this.getMeta();
	}

}
