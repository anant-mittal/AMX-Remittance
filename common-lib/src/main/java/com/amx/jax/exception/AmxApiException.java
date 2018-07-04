package com.amx.jax.exception;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

public abstract class AmxApiException extends AmxException {

	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	protected String errorKey;

	protected IExceptionEnum error;

	private Object meta;

	public AmxApiException() {
		super(null, null, true, false);
	}

	public AmxApiException(AmxApiError amxApiError) {
		this();
		this.meta = amxApiError.getMeta();
		try {
			this.error = getErrorIdEnum(amxApiError.getErrorId());
		} catch (Exception e) {
		}
		this.errorKey = amxApiError.getErrorId();
		this.errorMessage = amxApiError.getErrorMessage();
	}

	public AmxApiException(String errorMessage) {
		this();
		this.errorMessage = errorMessage;
	}

	public AmxApiException(String errorMessage, String errorCode) {
		this();
		this.errorMessage = errorMessage;
		this.errorKey = errorCode;
	}

	public AmxApiException(Exception e) {
		super(null, e, true, false);
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public AmxApiError createAmxApiError() {
		return new AmxApiError(this.getErrorKey(), this.getErrorMessage());
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

	public Object getMeta() {
		return meta;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}

	public abstract boolean isReportable();

}
