package com.amx.jax.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.error.JaxError;
import com.amx.jax.notification.alert.IAlert;
import com.amx.jax.util.JaxContextUtil;
import com.google.common.base.Throwables;

@ControllerAdvice
public class JaxControllerAdvice extends AmxAdvice {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext appContext;

	@ExceptionHandler(AbstractJaxException.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handle(AbstractJaxException ex, HttpServletRequest request,
			HttpServletResponse response) {
		raiseAlert(ex);
		return super.handle(ex, request, response);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handleException(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {
		String stackTrace = Throwables.getStackTraceAsString(ex);
		AbstractJaxException jaxException = new GlobalException(JaxError.JAX_SYSTEM_ERROR, ex);
		raiseAlert(jaxException);
		alert(ex);
		return super.handle(jaxException, request, response);
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
}
