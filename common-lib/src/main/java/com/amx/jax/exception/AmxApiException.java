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

	/**
	 * Finally resolves to statusKey
	 */
	protected IExceptionEnum error;

	/**
	 * Finally resolves to messageKey
	 */
	protected String errorKey;

	/**
	 * Finally resolves to message
	 */
	protected String errorMessage;

	AmxApiError apiError;

	public AmxApiException() {
		super(null, null, true, false);
	}

	public AmxApiException(AmxApiError amxApiError) {
		super(amxApiError.getMessage(), null, true, false);
		this.apiError = amxApiError;
		try {
			this.error = getErrorIdEnum(amxApiError.getErrorKey());
		} catch (Exception e) {
		}
		this.errorKey = amxApiError.getErrorKey();
		this.errorMessage = amxApiError.getMessage();
	}

	public AmxApiException(String errorMessage) {
		this();
		this.errorMessage = errorMessage;
	}

	public AmxApiException(IExceptionEnum error) {
		this();
		this.error = error;
	}

	public AmxApiException(IExceptionEnum error, String message) {
		super(message, null, true, false);
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
		if (this.apiError == null) {
			this.apiError = new AmxApiError(this.getErrorKey(), this.getErrorMessage());
			this.apiError.setException(this.getClass().getName());
		}
		return this.apiError;
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

	public Object getMeta() {
		if (this.apiError == null) {
			return null;
		}
		return this.apiError.getMeta();
	}

	public void setMeta(Object meta) {
		if (this.apiError == null) {
			this.apiError = createAmxApiError();
		}
		this.apiError.setMeta(meta);
	}

	public abstract boolean isReportable();

	public HttpStatus getHttpStatus() {
		return httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public static <T> T evaluate(Exception e) throws Exception {
		if (e instanceof AmxApiException) {
			throw (AmxApiException) e;
		} else if (e instanceof AmxException) {
			throw e;
		} else {
			throw e;
		}
	}
}
