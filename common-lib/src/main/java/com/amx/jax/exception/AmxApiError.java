package com.amx.jax.exception;

import com.amx.jax.api.AResponse;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiError extends AResponse<Object> {

	private String errorId;

	private String errorMessage;

	private String errorType; // IExceptionEnum

	public AmxApiError(String errorId, String errorMessage, IExceptionEnum errorType) {
		super();
		this.errorId = errorId;
		this.errorMessage = errorMessage;
		this.errorType = ArgUtil.parseAsString(errorType);
	}

	public AmxApiError(String errorId, String errorMessage) {
		super();
		this.errorId = errorId;
		this.errorMessage = errorMessage;
		this.errorType = null;
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

}
