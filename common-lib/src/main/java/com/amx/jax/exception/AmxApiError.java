package com.amx.jax.exception;

import com.amx.jax.api.AResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiError extends AResponse<Object> {

	private String errorId;

	public AmxApiError(String errorId, String errorMessage) {
		super();
		this.errorId = errorId;
		this.statusKey = errorId;
		this.message = errorMessage;
	}

	public AmxApiError() {
		super();
	}

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

}
