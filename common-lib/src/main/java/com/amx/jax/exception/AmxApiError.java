package com.amx.jax.exception;

import com.amx.jax.api.AResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiError extends AResponse<Object> {

	private String errorKey;

	public AmxApiError(String statusKey, String errorMessage) {
		super();
		this.errorKey = statusKey;
		this.statusKey = statusKey;
		this.message = errorMessage;
	}

	public AmxApiError() {
		super();
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

}
