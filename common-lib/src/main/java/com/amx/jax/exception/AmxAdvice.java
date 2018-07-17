package com.amx.jax.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.amx.jax.logger.LoggerService;

public abstract class AmxAdvice {

	private Logger logger = LoggerService.getLogger(AmxAdvice.class);

	@ExceptionHandler(AmxApiException.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handle(AmxApiException ex, HttpServletRequest request,
			HttpServletResponse response) {
		AmxApiError error = ex.createAmxApiError();
		error.setErrorClass(ex.getClass().getName());
		alert(ex);
		return new ResponseEntity<AmxApiError>(error, ex.getHttpStatus());
	}

	private void alert(AmxApiException ex) {
		logger.error("Exception occured in controller " + ex.getClass().getName() + " error message: "
				+ ex.getErrorMessage() + " error code: " + ex.getErrorKey(), ex);
	}

	private void alert(Exception ex) {
		logger.error("Exception occured in controller {} error message: {}", ex.getClass().getName(), ex.getMessage(),
				ex);
	}

	/**
	 * Handle all.
	 *
	 * @param ex
	 *            the ex
	 * @param request
	 *            the request
	 * @return the response entity
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<AmxApiError> handleAll(Exception ex, WebRequest request) {
		AmxApiError error = new AmxApiError();
		error.setException(ex.getClass().getName());
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		wrapper.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		wrapper.setException(ex.getClass().getName());
		postManService.notifyException(wrapper.getStatus(), ex);
		return new ResponseEntity<AmxApiError>(wrapper, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
