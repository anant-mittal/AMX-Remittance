package com.amx.jax.ui.config;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.amx.jax.api.AmxFieldError;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.logger.AuditService;
import com.amx.jax.model.AuthState;
import com.amx.jax.postman.PostManService;
import com.amx.jax.ui.audit.CAuthEvent;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.session.GuestSession;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;

/**
 * The Class WebJaxAdvice.
 */
@ControllerAdvice
public class WebJaxAdvice {

	/** The audit service. */
	@Autowired
	private AuditService auditService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/** The guest session. */
	@Autowired
	private GuestSession guestSession;

	/** The post man service. */
	@Autowired
	private PostManService postManService;

	/** The log. */
	private Logger LOG = LoggerFactory.getLogger(WebJaxAdvice.class);

	/**
	 * Handle.
	 *
	 * @param exc      the exc
	 * @param request  the request
	 * @param response the response
	 * @return the response entity
	 */
	@ExceptionHandler(AmxApiException.class)
	public ResponseEntity<ResponseWrapper<Object>> handle(AmxApiException exc, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();

		wrapper.setMessage(OWAStatusStatusCodes.UNKNOWN_JAX_ERROR, exc);

		String errorKey = ArgUtil.parseAsString(exc.getErrorKey(), OWAStatusStatusCodes.UNKNOWN_JAX_ERROR.toString());
		if (exc.isReportable()) {
			LOG.error(errorKey, exc);
			postManService.notifyException(errorKey, exc);
		} else {
			LOG.error(ArgUtil.parseAsString(errorKey, exc.getErrorMessage()));
		}

		AuthState state = guestSession.getState();
		if (state.getFlow() != null) {
			auditService.log(new CAuthEvent(state, CAuthEvent.Result.FAIL, exc.getError()));
		}
		if (exc.getError() == JaxError.USER_LOGIN_ATTEMPT_EXCEEDED
				|| JaxError.UNAUTHORIZED.equals(exc.getError())) {
			sessionService.unIndexUser();
		}

		wrapper.setException(exc.getClass().getName());

		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.OK);
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
	public ResponseEntity<ResponseWrapper<Object>> handle(ConstraintViolationException exception) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		for (ConstraintViolation<?> responseError : exception.getConstraintViolations()) {
			AmxFieldError newError = new AmxFieldError();
			newError.setField(responseError.getPropertyPath().toString());
			newError.setDescription(HttpUtils.sanitze(responseError.getMessage()));
			errors.add(newError);
		}
		wrapper.setErrors(errors);
		wrapper.setStatusEnum(OWAStatusStatusCodes.BAD_INPUT);
		wrapper.setException(exception.getClass().getName());
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle.
	 *
	 * @param exception the exception
	 * @return the response entity
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseWrapper<Object>> handle(HttpMessageNotReadableException exception) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		AmxFieldError newError = new AmxFieldError();
		newError.setDescription(HttpUtils.sanitze(exception.getMessage()));
		errors.add(newError);
		wrapper.setErrors(errors);
		wrapper.setStatusEnum(OWAStatusStatusCodes.BAD_INPUT);
		wrapper.setException(exception.getClass().getName());
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle.
	 *
	 * @param ex       the ex
	 * @param request  the request
	 * @param response the response
	 * @return the response entity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<ResponseWrapper<Object>> handle(MethodArgumentNotValidException ex,
			HttpServletRequest request, HttpServletResponse response) {
		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			AmxFieldError newError = new AmxFieldError();
			newError.setField(error.getField());
			newError.setDescription(HttpUtils.sanitze(error.getDefaultMessage()));
			errors.add(newError);
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			AmxFieldError newError = new AmxFieldError();
			newError.setObzect(error.getObjectName());
			newError.setDescription(HttpUtils.sanitze(error.getDefaultMessage()));
			errors.add(newError);
		}
		return notValidArgument(ex, errors, request, response);
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
	protected ResponseEntity<ResponseWrapper<Object>> handle(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request, HttpServletResponse response) {
		List<AmxFieldError> errors = new ArrayList<AmxFieldError>();
		AmxFieldError newError = new AmxFieldError();
		newError.setField(ex.getName());
		newError.setDescription(HttpUtils.sanitze(ex.getMessage()));
		errors.add(newError);
		return notValidArgument(ex, errors, request, response);
	}

	/**
	 * Not valid argument.
	 *
	 * @param ex       the ex
	 * @param errors   the errors
	 * @param request  the request
	 * @param response the response
	 * @return the response entity
	 */
	protected ResponseEntity<ResponseWrapper<Object>> notValidArgument(Exception ex, List<AmxFieldError> errors,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		wrapper.setStatusEnum(OWAStatusStatusCodes.BAD_INPUT);
		wrapper.setErrors(errors);
		wrapper.setException(ex.getClass().getName());
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle all.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ResponseWrapper<Object>> handleAll(Exception ex, WebRequest request) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		wrapper.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		wrapper.setException(ex.getClass().getName());
		LOG.error("In Advice Exception Captured", ex);
		postManService.notifyException(wrapper.getStatus(), ex);
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
