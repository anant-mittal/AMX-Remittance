package com.amx.jax.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.jax.JaxFieldValidationException;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.notification.alert.IAlert;
import com.amx.jax.util.JaxContextUtil;

@ControllerAdvice
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger logger = Logger.getLogger(GlobalControllerExceptionHandler.class);
	@Autowired
	private ApplicationContext appContext;

	@ExceptionHandler(AbstractJaxException.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handle(AbstractJaxException ex, HttpServletRequest request,
			HttpServletResponse response) {
		AmxApiError error = ex.createAmxApiError();
		error.setErrorClass(ex.getClass().getName());
		error.setMeta(ex.getMeta());
		logger.info("Exception occured in controller " + ex.getClass().getName() + " error message: "
				+ ex.getErrorMessage() + " error code: " + ex.getErrorKey(), ex);
		raiseAlert(ex);
		return new ResponseEntity<AmxApiError>(error, ex.getHttpStatus());
	}

	private void raiseAlert(AbstractJaxException ex) {
		JaxEvent event = JaxContextUtil.getJaxEvent();
		if (event != null) {
			IAlert alert = appContext.getBean(event.getAlertBean());
			if (alert.isEnabled()) {
				alert.sendAlert(ex);
			}
		}
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		JaxFieldValidationException exception = new JaxFieldValidationException(
				processFieldErrors(ex.getBindingResult()));
		AmxApiError error = exception.createAmxApiError();
		error.setErrorClass(ex.getClass().getName());
		// JaxFieldError validationErrorField = new
		// JaxFieldError(ex.getBindingResult().getFieldError().getField());
		// errors.get(0).setValidationErrorField(validationErrorField);
		// setErrorHeaders(error);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	private String processFieldErrors(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		sb.append(bindingResult.getFieldError().getField()).append(" ");
		sb.append(bindingResult.getFieldError().getDefaultMessage());
		return sb.toString();
	}
}
