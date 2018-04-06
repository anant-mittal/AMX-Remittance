package com.amx.jax.ui.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AbstractException;
import com.amx.jax.logger.AuditService;
import com.amx.jax.ui.auth.CAuthEvent;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.response.ResponseError;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.HttpService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.session.GuestSession;
import com.amx.utils.ArgUtil;

@ControllerAdvice
public class WebJaxAdvice {

	@Autowired
	private AuditService auditService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private GuestSession guestSession;

	private Logger LOG = LoggerFactory.getLogger(WebJaxAdvice.class);

	@ExceptionHandler(AbstractException.class)
	public ResponseEntity<ResponseWrapper<Object>> handle(AbstractException exc, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		wrapper.setMessage(WebResponseStatus.UNKNOWN_JAX_ERROR, exc);
		if (exc.getError() == null && exc.getError() == JaxError.UNKNOWN_JAX_ERROR) {
			LOG.error(ArgUtil.parseAsString(exc.getErrorKey(), WebResponseStatus.UNKNOWN_JAX_ERROR.toString()), exc);
		} else {
			LOG.error(ArgUtil.parseAsString(exc.getErrorKey(), WebResponseStatus.UNKNOWN_JAX_ERROR.toString()));
		}

		AuthState state = guestSession.getState();
		if (state.getFlow() != null) {
			auditService.log(new CAuthEvent(state, CAuthEvent.Result.FAIL, exc.getError()));
		}
		if (exc.getError() == JaxError.USER_LOGIN_ATTEMPT_EXCEEDED) {
			sessionService.unIndexUser();
		}
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.OK);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseWrapper<Object>> handle(ConstraintViolationException exception) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		List<ResponseError> errors = new ArrayList<ResponseError>();
		for (ConstraintViolation<?> responseError : exception.getConstraintViolations()) {
			ResponseError newError = new ResponseError();
			newError.setField(responseError.getPropertyPath().toString());
			newError.setDescription(responseError.getMessage());
			errors.add(newError);
		}
		wrapper.setErrors(errors);
		wrapper.setStatus(WebResponseStatus.BAD_INPUT);
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.BAD_REQUEST);
		// return
		// error(exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
		// .collect(Collectors.toList()));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseWrapper<Object>> handle(HttpMessageNotReadableException exception) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		List<ResponseError> errors = new ArrayList<ResponseError>();
		ResponseError newError = new ResponseError();
		// newError.setField(exception.get);
		newError.setDescription(HttpService.sanitze(exception.getMessage()));
		errors.add(newError);
		wrapper.setErrors(errors);
		wrapper.setStatus(WebResponseStatus.BAD_INPUT);
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.BAD_REQUEST);
		// return
		// error(exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
		// .collect(Collectors.toList()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<ResponseWrapper<Object>> handle(MethodArgumentNotValidException ex,
			HttpServletRequest request, HttpServletResponse response) {
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
		wrapper.setStatus(WebResponseStatus.BAD_INPUT);
		wrapper.setErrors(errors);
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.BAD_REQUEST);
		// return handleExceptionInternal(ex, wrapper, headers, HttpStatus.BAD_REQUEST,
		// request);
		// return new ResponseEntity<Object>(wrapper, HttpStatus.OK);
	}

}
