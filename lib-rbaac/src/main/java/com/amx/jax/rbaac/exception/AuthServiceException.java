package com.amx.jax.rbaac.exception;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.util.JaxUtil;

/**
 * The Class AuthServiceException.
 */
public class AuthServiceException extends AmxApiException {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceException.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new auth service exception.
	 *
	 * @param errorMessage
	 *            the error message
	 */
	public AuthServiceException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new auth service exception.
	 *
	 * @param errorMessage
	 *            the error message
	 * @param errorCode
	 *            the error code
	 */
	public AuthServiceException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}

	/**
	 * Instantiates a new auth service exception.
	 *
	 * @param errorMessage
	 *            the error message
	 * @param error
	 *            the error
	 */
	public AuthServiceException(String errorMessage, RbaacServiceError error) {
		super(errorMessage, error.getStatusKey());
	}

	/**
	 * Instantiates a new auth service exception.
	 *
	 * @param error
	 *            the error
	 */
	public AuthServiceException(AmxApiError error) {
		super(error);
	}

	/**
	 * Instantiates a new auth service exception.
	 *
	 * @param error
	 *            the error
	 * @param expressions
	 *            the expressions
	 */
	public AuthServiceException(RbaacServiceError error, Object... expressions) {
		JaxUtil util = new JaxUtil();
		List<String> list = Arrays.asList(expressions).stream().map(i -> i.toString()).collect(Collectors.toList());
		this.errorKey = util.buildErrorExpressions(error.getStatusKey(), list);

	}

	/**
	 * Instantiates a new auth service exception.
	 *
	 * @param errorMessage
	 *            the error message
	 * @param error
	 *            the error
	 * @param expressions
	 *            the expressions
	 */
	public AuthServiceException(String errorMessage, RbaacServiceError error, Object... expressions) {
		JaxUtil util = new JaxUtil();
		List<String> list = Arrays.asList(expressions).stream().map(i -> i.toString()).collect(Collectors.toList());
		this.errorKey = util.buildErrorExpressions(error.getStatusKey(), list);
		this.errorMessage = errorMessage;

	}

	/**
	 * Instantiates a new auth service exception.
	 *
	 * @param e
	 *            the e
	 */
	public AuthServiceException(Exception e) {
		super(e);
	}

	/*
	 * Instantiates a new auth service exception.
	 *
	 * @param errorMessage the error message
	 * 
	 * @param error the error
	 * 
	 * @param e the e
	 */
	public AuthServiceException(String errorMessage, RbaacServiceError error, Exception e) {
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
			Constructor<? extends AuthServiceException> constructor = this.getClass().getConstructor(AmxApiError.class);
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
		return RbaacServiceError.valueOf(errorId);
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
