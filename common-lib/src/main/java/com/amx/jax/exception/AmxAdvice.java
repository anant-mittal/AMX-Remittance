package com.amx.jax.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.amx.jax.api.AmxFieldError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.service.HttpService;

public abstract class AmxAdvice {

	private Logger logger = LoggerService.getLogger(AmxAdvice.class);

	@ExceptionHandler(AmxApiException.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handle(AmxApiException ex, HttpServletRequest request,
			HttpServletResponse response) {
		AmxApiError error = ex.createAmxApiError();
		error.setException(ex.getClass().getName());
		error.setMeta(ex.getMeta());
		logger.info("Exception occured in controller " + ex.getClass().getName() + " error message: "
				+ ex.getErrorMessage() + " error code: " + ex.getErrorKey(), ex);
		alert(ex);
		return new ResponseEntity<AmxApiError>(error, ex.getHttpStatus());
	}

	private void alert(AmxApiException ex) {
		logger.error("Exception occured in controller " + ex.getClass().getName() + " error message: "
				+ ex.getErrorMessage() + " error code: " + ex.getErrorKey(), ex);
	}

	public void alert(Exception ex) {
		logger.error("Exception occured in controller {} error message: {}", ex.getClass().getName(), ex.getMessage(),
				ex);
	}

	protected ResponseEntity<AmxApiError> badRequest(Exception ex, List<AmxFieldError> errors,
			HttpServletRequest request, HttpServletResponse response) {
		AmxApiError apiError = new AmxApiError();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setErrors(errors);
		apiError.setException(ex.getClass().getName());
		return new ResponseEntity<AmxApiError>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<AmxApiError> handle(MethodArgumentNotValidException ex, HttpServletRequest request,
			HttpServletResponse response) {

		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			AmxFieldError newError = new AmxFieldError();
			newError.setField(error.getField());
			newError.setDescription(HttpService.sanitze(error.getDefaultMessage()));
			errors.add(newError);
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			AmxFieldError newError = new AmxFieldError();
			newError.setObzect(error.getObjectName());
			newError.setDescription(HttpService.sanitze(error.getDefaultMessage()));
			errors.add(newError);
		}
		return badRequest(ex, errors, request, response);
	}

	/**
	 * Handle.
	 *
	 * @param ex
	 *            the ex
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the response entity
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<AmxApiError> handle(MethodArgumentTypeMismatchException ex, HttpServletRequest request,
			HttpServletResponse response) {
		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		AmxFieldError newError = new AmxFieldError();
		newError.setField(ex.getName());
		newError.setDescription(HttpService.sanitze(ex.getMessage()));
		errors.add(newError);
		return badRequest(ex, errors, request, response);
	}

	/**
	 * Handle.
	 *
	 * @param exception
	 *            the exception
	 * @return the response entity
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<AmxApiError> handle(ConstraintViolationException exception, HttpServletRequest request,
			HttpServletResponse response) {
		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		for (ConstraintViolation<?> responseError : exception.getConstraintViolations()) {
			AmxFieldError newError = new AmxFieldError();
			newError.setField(responseError.getPropertyPath().toString());
			newError.setDescription(HttpService.sanitze(responseError.getMessage()));
			errors.add(newError);
		}
		return badRequest(exception, errors, request, response);
	}
}
