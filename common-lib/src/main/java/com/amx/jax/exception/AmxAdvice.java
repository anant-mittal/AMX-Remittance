package com.amx.jax.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.amx.jax.AppConstants;
import com.amx.jax.api.AmxFieldError;
import com.amx.jax.exception.ApiHttpExceptions.ApiHttpArgException;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.logger.events.ApiAuditEvent;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;

public abstract class AmxAdvice {

	private Logger logger = LoggerService.getLogger(AmxAdvice.class);

	@Autowired
	private AuditService auditService;

	@ExceptionHandler(AmxApiException.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handle(AmxApiException ex, HttpServletRequest request,
			HttpServletResponse response) {
		AmxApiError apiError = ex.createAmxApiError();
		apiError.setException(ex.getClass().getName());
		apiError.setStatusEnum(ex.getError());
		apiError.setMeta(ex.getMeta());
		apiError.setMessage(ArgUtil.ifNotEmpty(apiError.getMessage(), ex.getMessage(), ex.getErrorMessage()));
		apiError.setPath(request.getRequestURI());
		ExceptionMessageKey.resolveLocalMessage(apiError);
		response.setHeader(AppConstants.EXCEPTION_HEADER_KEY, apiError.getException());
		alert(ex);
		return new ResponseEntity<AmxApiError>(apiError, getHttpStatus(ex));
	}

	public HttpStatus getHttpStatus(AmxApiException exp) {
		return exp.getHttpStatus();
	}

	private void alert(AmxApiException ex) {
		auditService.log(new ApiAuditEvent(ex), ex);
	}

	public void alert(Exception ex) {
		logger.error("Exception occured in controller {} error message: {}", ex.getClass().getName(), ex.getMessage(),
				ex);
	}

	protected ResponseEntity<AmxApiError> badRequest(Exception ex, List<AmxFieldError> errors,
			HttpServletRequest request, HttpServletResponse response, ApiStatusCodes statusKey) {
		AmxApiError apiError = new AmxApiError();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		// apiError.setMessage(ex.getMessage());
		apiError.setStatusKey(statusKey.toString());
		apiError.setErrors(errors);
		apiError.setException(ApiHttpArgException.class.getName());
		ExceptionMessageKey.resolveLocalMessage(apiError);
		response.setHeader(AppConstants.EXCEPTION_HEADER_KEY, apiError.getException());
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
			newError.setObzect(error.getObjectName());
			newError.setField(error.getField());
			newError.setDescription(HttpUtils.sanitze(error.getDefaultMessage()));
			newError.setCode(error.getCode());
			errors.add(newError);
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			AmxFieldError newError = new AmxFieldError();
			newError.setObzect(error.getObjectName());
			newError.setDescription(HttpUtils.sanitze(error.getDefaultMessage()));
			errors.add(newError);
		}
		return badRequest(ex, errors, request, response, ApiStatusCodes.PARAM_INVALID);
	}

	/**
	 * Handle.
	 *
	 * @param ex       the ex
	 * @param request  the request
	 * @param response the response
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
		newError.setDescription(HttpUtils.sanitze(ex.getMessage()));
		errors.add(newError);
		return badRequest(ex, errors, request, response, ApiStatusCodes.PARAM_TYPE_MISMATCH);
	}

	/**
	 * Handle.
	 *
	 * @param exception the exception
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
			newError.setDescription(HttpUtils.sanitze(responseError.getMessage()));
			errors.add(newError);
		}
		return badRequest(exception, errors, request, response, ApiStatusCodes.PARAM_ILLEGAL);
	}
}
