package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amx.amxlib.exception.AbstractException;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;

@ControllerAdvice
public class JaxAdvice {

	private Logger LOG = LoggerFactory.getLogger(JaxAdvice.class);

	@ExceptionHandler(AbstractException.class)
	public ResponseEntity<ResponseWrapper<Object>> handle(AbstractException exc, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		wrapper.setMessage(ResponseStatus.UNKNOWN_JAX_ERROR, exc);
		LOG.error(ResponseStatus.UNKNOWN_JAX_ERROR.toString(), exc);
		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.OK);
	}

}
