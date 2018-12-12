package com.amx.jax.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.notification.alert.IAlert;
import com.amx.jax.util.JaxContextUtil;

@ControllerAdvice
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class JaxControllerAdvice extends AmxAdvice {

	private Logger logger = Logger.getLogger(JaxControllerAdvice.class);
	@Autowired
	private ApplicationContext appContext;

	@ExceptionHandler(AbstractJaxException.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handle(AbstractJaxException ex, HttpServletRequest request,
			HttpServletResponse response) {
		raiseAlert(ex);
		return super.handle(ex, request, response);
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

	/*
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<AmxApiError> handle(MethodArgumentNotValidException ex, HttpServletRequest request,
			HttpServletResponse response) {
		JaxFieldValidationException exception = new JaxFieldValidationException(
				processFieldErrors(ex.getBindingResult()));
		AmxApiError error = exception.createAmxApiError();
		error.setException(exception.getClass().getName());
		logger.info("Exception occured in controller " + exception.getClass().getName() + " error message: "
				+ exception.getErrorMessage() + " error code: " + exception.getErrorKey(), ex);
		return new ResponseEntity<AmxApiError>(error, HttpStatus.BAD_REQUEST);
	}

	private String processFieldErrors(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		// sb.append(bindingResult.getFieldError().getField()).append(" ");
		sb.append(bindingResult.getFieldError().getDefaultMessage());
		return sb.toString();
	}
	*/
}
