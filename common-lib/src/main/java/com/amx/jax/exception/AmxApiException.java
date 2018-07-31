package com.amx.jax.exception;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import com.amx.jax.logger.LoggerService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

public abstract class AmxApiException extends AmxException {

	private static final Logger LOGGER = LoggerService.getLogger(AmxApiException.class);

	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	protected String errorKey;

	protected IExceptionEnum error;

	protected HttpStatus httpStatus;

	public AmxApiException() {
		super(null, null, true, false);
	}

	public AmxApiException(AmxApiError error) {
		this();
		try {
			this.error = getErrorIdEnum(error.getErrorId());
		} catch (Exception e) {
		}
		this.errorKey = error.getErrorId();
		this.errorMessage = error.getErrorMessage();
	}

	public AmxApiException(String errorMessage) {
		this();
		this.errorMessage = errorMessage;
	}

	public AmxApiException(IExceptionEnum error) {
		this();
		this.error = error;
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
	public AmxApiException getInstance(AmxApiError apiError) {
		try {
			Constructor<? extends AmxApiException> constructor = this.getClass().getConstructor(AmxApiError.class);
			return constructor.newInstance(apiError);

		} catch (Exception e) {
			LOGGER.error("error occured in getinstance method", e);
		}
		return null;
	}

	public abstract IExceptionEnum getErrorIdEnum(String errorId);

	public abstract boolean isReportable();

	public HttpStatus getHttpStatus() {
		return httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
