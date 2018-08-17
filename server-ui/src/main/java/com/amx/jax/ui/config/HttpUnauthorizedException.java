package com.amx.jax.ui.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class HttpUnauthorizedException.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class HttpUnauthorizedException extends AuthenticationException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant UN_SEQUENCE. */
	public static final String UN_SEQUENCE = "UnAuthrized Sequence";

	/** The Constant UN_AUTHORIZED. */
	public static final String UN_AUTHORIZED = "UnAuthrized Access";

	/**
	 * Instantiates a new http unauthorized exception.
	 */
	public HttpUnauthorizedException() {
		super("Bad Security Tokens");
	}

	/**
	 * Instantiates a new http unauthorized exception.
	 *
	 * @param msg
	 *            the msg
	 */
	public HttpUnauthorizedException(String msg) {
		super(msg);
	}

}
