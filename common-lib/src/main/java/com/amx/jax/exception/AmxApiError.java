package com.amx.jax.exception;

import com.amx.jax.api.AResponse;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiError extends AResponse<Object> {

	private String errorKey;

	public AmxApiError(String errorKey, String errorMessage) {
		super();
		this.errorKey = errorKey;
		this.message = errorMessage;
	}

	public AmxApiError(IExceptionEnum error, String errorKey, String errorMessage) {
		super();
		this.statusKey = ArgUtil.isEmpty(error) ? null : error.getStatusKey();
		this.errorKey = errorKey;
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
