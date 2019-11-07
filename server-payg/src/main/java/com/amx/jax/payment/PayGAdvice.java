package com.amx.jax.payment;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.api.AmxFieldError;
import com.amx.jax.exception.AmxAdvice;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.AmxException;
import com.amx.jax.exception.ApiHttpExceptions.ApiHttpArgException;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.exception.ExceptionMessageKey;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.HttpUtils;

@ControllerAdvice
public class PayGAdvice extends AmxAdvice {

	private Logger logger = LoggerService.getLogger(PayGAdvice.class);

	@Autowired
	AppConfig appConfig;

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	protected ResponseEntity<AmxApiError> handle(AccessDeniedException ex, HttpServletRequest request,
			HttpServletResponse response) {
		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		AmxFieldError newError = new AmxFieldError();
//		newError.setField(ex.getName());
		newError.setDescription(HttpUtils.sanitze(ex.getMessage()));
		errors.add(newError);
		return accessDenied(ex, errors, request, response, ApiStatusCodes.ACCESS_DENIED);
	}

	protected ResponseEntity<AmxApiError> accessDenied(Exception ex, List<AmxFieldError> errors,
			HttpServletRequest request, HttpServletResponse response, ApiStatusCodes statusKey) {
		AmxApiError apiError = new AmxApiError();
		apiError.setHttpStatus(HttpStatus.FORBIDDEN);
		// apiError.setMessage(ex.getMessage());
		apiError.setStatusKey(statusKey.toString());
		apiError.setErrors(errors);
		apiError.setException(ApiHttpArgException.class.getName());
		ExceptionMessageKey.resolveLocalMessage(apiError);
		response.setHeader(AppConstants.EXCEPTION_HEADER_KEY, apiError.getException());
		return new ResponseEntity<AmxApiError>(apiError, HttpStatus.FORBIDDEN);
	}

	@Override
	public HttpStatus getHttpStatus(AmxApiException exp) {
		if (appConfig.isAppResponseOK()) {
			return HttpStatus.OK;
		}
		return super.getHttpStatus(exp);
	}

	@ExceptionHandler({ Exception.class })
	// @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		logger.error("In Advice Exception Captured", ex);
		// throw new AmxException("hi", null,true, false);
		// postManService.notifyException(wrapper.getStatus(), ex);
		return new ResponseEntity<Object>(""
				+ "<!DOCTYPE html>\n" +
				"<html>\n" +
				"<body><center>\n" +
				"<c>Something went wrong! </h1>\n" +
				"<h2>Our Engineers are on it</h2>\n" +
				"</center></body>\n" +
				"</html>", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
