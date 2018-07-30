package com.amx.jax.auth.exception;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.amx.jax.auth.error.AuthServiceError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.util.JaxUtil;

public class AuthServiceException extends AmxApiException {

	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceException.class);

	private static final long serialVersionUID = 1L;

	public AuthServiceException(String errorMessage) {
		super(errorMessage);
	}

	public AuthServiceException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}

	public AuthServiceException(String errorMessage, AuthServiceError error) {
		super(errorMessage, error.getCode());
	}

	public AuthServiceException(AmxApiError error) {
		super(error);
	}

	public AuthServiceException(AuthServiceError error, Object... expressions) {
		JaxUtil util = new JaxUtil();
		List<String> list = Arrays.asList(expressions).stream().map(i -> i.toString()).collect(Collectors.toList());
		this.errorKey = util.buildErrorExpressions(error.getCode(), list);

	}

	public AuthServiceException(String errorMessage, AuthServiceError error, Object... expressions) {
		JaxUtil util = new JaxUtil();
		List<String> list = Arrays.asList(expressions).stream().map(i -> i.toString()).collect(Collectors.toList());
		this.errorKey = util.buildErrorExpressions(error.getCode(), list);
		this.errorMessage = errorMessage;

	}

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

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return AuthServiceError.valueOf(errorId);
	}

	@Override
	public boolean isReportable() {
		return false;
	}
}
