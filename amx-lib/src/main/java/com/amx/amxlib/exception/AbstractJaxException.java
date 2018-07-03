package com.amx.amxlib.exception;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.amx.amxlib.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;

public abstract class AbstractJaxException extends AmxApiException {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AbstractJaxException.class);
	private Object meta;

	public AbstractJaxException(Exception e) {
		super(e);
	}

	public AbstractJaxException(AmxApiError error) {
		super(error);
		this.meta = error.getMeta();
	}

	public AbstractJaxException(String errorMessage) {
		super(errorMessage);
	}

	public AbstractJaxException(String errorMessage, String errorKey) {
		super(errorMessage, errorKey);
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

	public Object getMeta() {
		return meta;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}

	@Override
	public boolean isReportable() {
		return false;
	}
}
