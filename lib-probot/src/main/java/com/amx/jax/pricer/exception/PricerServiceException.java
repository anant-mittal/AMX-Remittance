package com.amx.jax.pricer.exception;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;

import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.logger.LoggerService;

/**
 * The Class PricerServiceException.
 */
public class PricerServiceException extends AmxApiException {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(PricerServiceException.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public PricerServiceException(String errorMessage) {
		super(errorMessage);
	}

	public PricerServiceException(PricerServiceError error, String errorMessage) {
		// super(errorMessage, error.getStatusKey());
		super(error, errorMessage);
	}

	public PricerServiceException(AmxApiError error) {
		super(error);
	}

	public PricerServiceException(Exception e) {
		super(e);
	}

	public PricerServiceException(String errorMessage, PricerServiceError error, Exception e) {
		super(e);
		this.errorMessage = errorMessage;
		this.errorKey = error.getStatusKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exception.AmxApiException#getInstance(com.amx.jax.exception.
	 * AmxApiError)
	 */
	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		try {
			Constructor<? extends PricerServiceException> constructor = this.getClass()
					.getConstructor(AmxApiError.class);
			return constructor.newInstance(apiError);

		} catch (Exception e) {
			LOGGER.error("error occured in getinstance method", e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exception.AmxApiException#getErrorIdEnum(java.lang.String)
	 */
	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return PricerServiceError.valueOf(errorId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exception.AmxApiException#isReportable()
	 */
	@Override
	public boolean isReportable() {
		return false;
	}
}
