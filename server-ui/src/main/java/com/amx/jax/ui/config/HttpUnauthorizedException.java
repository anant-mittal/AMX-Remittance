package com.amx.jax.ui.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class HttpUnauthorizedException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	public static final String UN_SEQUENCE = "UnAuthrized Sequence";
	public static final String UN_AUTHORIZED = "UnAuthrized Access";

	public HttpUnauthorizedException() {
		super("Bad Security Tokens");
	}

	public HttpUnauthorizedException(String msg) {
		super(msg);
	}

}
