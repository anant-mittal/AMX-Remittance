package com.amx.jax.ui.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amx.jax.ui.response.ResponseError;
import com.amx.jax.ui.response.ResponseWrapper;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	protected ResponseEntity<Object> handleMethodArgumentNotValid2(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}
	**/

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();

		List<ResponseError> errors = new ArrayList<ResponseError>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			ResponseError newError = new ResponseError();
			newError.setField(error.getField());
			newError.setDescription(error.getDefaultMessage());
			errors.add(newError);
		}

		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			ResponseError newError = new ResponseError();
			newError.setObzect(error.getObjectName());
			newError.setDescription(error.getDefaultMessage());
			errors.add(newError);
		}
		wrapper.setErrors(errors);
		return handleExceptionInternal(ex, wrapper, headers,HttpStatus.BAD_REQUEST, request);
		//return new ResponseEntity<Object>(wrapper, HttpStatus.OK);
	}
}
