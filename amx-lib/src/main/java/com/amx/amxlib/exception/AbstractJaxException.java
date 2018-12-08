package com.amx.amxlib.exception;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;

public abstract class AbstractJaxException extends com.amx.jax.exception.AbstractJaxException {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AbstractJaxException.class);

	public AbstractJaxException(Exception e) {
		super(e);
	}

	public AbstractJaxException(AmxApiError error) {
		super(error);
	}

	public AbstractJaxException(String errorMessage) {
		super(errorMessage);
	}

	public AbstractJaxException(String errorMessage, String errorKey) {
		super(errorMessage, errorKey);
	}

	public AbstractJaxException(JaxError error, String errorMessage) {
		super(error, errorMessage);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		try {
			Constructor<? extends AbstractJaxException> constructor = this.getClass().getConstructor(AmxApiError.class);
			return constructor.newInstance(apiError);

		} catch (Exception e) {
			LOGGER.error("error occured in getinstance method", e);
		}
		return null;
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return JaxError.valueOf(errorId);
	}

	public AbstractJaxException() {
		super();
	}

	@Override
	public boolean isReportable() {
		return false;
	}
}
