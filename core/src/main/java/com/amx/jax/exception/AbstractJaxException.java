package com.amx.jax.exception;

public abstract class AbstractJaxException extends AmxApiException {

	private static final long serialVersionUID = 1L;

	protected String errorCode;

	public AbstractJaxException() {
		super();
	}

	public AbstractJaxException(String errorMessage) {
		super(errorMessage);
	}

	public AbstractJaxException(String errorMessage, String errorCode) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

	public AbstractJaxException(AmxApiError error) {
		super(error);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		return null;
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return null;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
