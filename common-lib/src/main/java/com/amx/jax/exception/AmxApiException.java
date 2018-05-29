package com.amx.jax.exception;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

public abstract class AmxApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	protected String errorKey;

	protected IExceptionEnum error;

	public AmxApiException() {
		super();
	}

	public AmxApiException(AmxApiError error) {
		try {
			this.error = getErrorIdEnum(error.getErrorId());
		} catch (Exception e) {
		}
		this.errorKey = error.getErrorId();
		this.errorMessage = error.getErrorMessage();
	}

	public AmxApiException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public AmxApiException(String errorMessage, String errorCode) {
		super();
		this.errorMessage = errorMessage;
		this.errorKey = errorCode;
	}

	public AmxApiException(Exception e) {
		super(e);
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public IExceptionEnum getError() {
		return error;
	}

	public void setError(IExceptionEnum error) {
		this.error = error;
	}

	public String getErrorKey() {
		if (!ArgUtil.isEmptyString(errorKey)) {
			return errorKey;
		} else {
			return ArgUtil.parseAsString(error, Constants.BLANK);
		}
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	/**
	 * Should return fresh new instance of exception
	 * 
	 * @param apiError
	 * 
	 * @return
	 */
	public abstract AmxApiException getInstance(AmxApiError apiError);

	public abstract IExceptionEnum getErrorIdEnum(String errorId);
}
