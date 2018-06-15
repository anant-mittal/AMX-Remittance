package com.amx.jax.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.amx.amxlib.error.JaxError;
import com.amx.jax.util.JaxUtil;

public class GlobalException extends AbstractJaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GlobalException(String errorMessage) {
		super(errorMessage);
	}

	public GlobalException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}

	public GlobalException(String errorMessage, JaxError error) {
		super(errorMessage, error.getCode());
	}

	public GlobalException(JaxError error, Object... expressions) {
		JaxUtil util = new JaxUtil();
		List<String> list = Arrays.asList(expressions).stream().map(i -> i.toString()).collect(Collectors.toList());
		this.errorCode = util.buildErrorExpressions(error.getCode(), list);

	}
	
	public GlobalException(String errorMessage, JaxError error, Object... expressions) {
		JaxUtil util = new JaxUtil();
		List<String> list = Arrays.asList(expressions).stream().map(i -> i.toString()).collect(Collectors.toList());
		this.errorCode = util.buildErrorExpressions(error.getCode(), list);
		this.errorMessage = errorMessage;

	}
}
