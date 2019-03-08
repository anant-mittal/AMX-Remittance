package com.amx.jax.exception;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

	Map<String, String> detailMap = null;

	public AmxApiException() {
		super(null, null, true, false);
	}

	public AmxApiException(AmxApiError amxApiError) {
		super(amxApiError.getMessage(), null, true, false);
		this.apiError = amxApiError;
		try {
			this.error = getErrorIdEnum(amxApiError.getStatusKey());
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

	public AmxApiException(IExceptionEnum error, String errorMessage) {
		super(errorMessage, null, true, false);
		this.error = error;
		this.errorKey = error.getStatusKey();
		this.errorMessage = errorMessage;
	}

	/**
	 * @deprecated Declare IExceptionEnum for String errorCode, and pass the message
	 * @param errorCode
	 * @param errorMessage
	 */
	@Deprecated
	public AmxApiException(String errorCode, String errorMessage) {
		this();
		this.errorKey = errorCode;
		this.errorMessage = errorMessage;
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
			this.apiError = new AmxApiError(this.error, this.getErrorKey(), this.getErrorMessage());
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

	public Map<String, String> getDetailMap() {
		if (detailMap == null) {
			detailMap = new HashMap<String, String>();
		}
		return detailMap;
	}

	public void setDetailMap(Map<String, String> detailMap) {
		this.detailMap = detailMap;
	}

	/**
	 * This detail is useful for detailed debugging/audit, it is nver sent to ui
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public AmxApiException put(String key, String value) {
		this.getDetailMap().put(key, value);
		return this;
	}

	/**
	 * Same as {@link #put(String, String)}
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public AmxApiException put(String key, BigDecimal value) {
		this.getDetailMap().put(key, ArgUtil.parseAsString(value));
		return this;
	}

	/**
	 * Same as {@link #put(String, String)}
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public AmxApiException put(String key, Long value) {
		this.getDetailMap().put(key, ArgUtil.parseAsString(value));
		return this;
	}

	/**
	 * Same as {@link #put(String, String)}
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public AmxApiException put(String key, Integer value) {
		this.getDetailMap().put(key, ArgUtil.parseAsString(value));
		return this;
	}

	/**
	 * Same as {@link #put(String, String)}
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public <E extends Enum<E>> AmxApiException put(String key, E value) {
		this.getDetailMap().put(key, ArgUtil.parseAsString(value));
		return this;
	}

	/**
	 * To set meta values, for exception, possible cases : Prefix Value in case OTP
	 * is required for the api to complete its task
	 * 
	 * @param meta
	 * @return
	 */
	public AmxApiException meta(Object meta) {
		this.setMeta(meta);
		return this;
	}
}
