package com.amx.jax.sso;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.amx.jax.AppConstants;
import com.amx.jax.api.AmxFieldError;
import com.amx.jax.exception.AmxAdvice;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.ApiHttpExceptions.ApiHttpArgException;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.exception.ExceptionMessageKey;
import com.amx.utils.HttpUtils;

@ControllerAdvice
public class SSOAdvice extends AmxAdvice {
	
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

}
