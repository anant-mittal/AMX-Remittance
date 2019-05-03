package com.amx.jax.util;

import org.apache.commons.lang.StringUtils;

import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;

public final class ParamValidator {

	/**
	 * validate null objects
	 * 
	 * @param param
	 * @param message
	 */
	public static void validateNotNull(Object param, String message) {
		if (param == null) {
			throw new AuthServiceException(RbaacServiceError.PARAM_INVALID, message);
		}
	}

	public static void validateNotEmpty(String param, String message) {
		if (StringUtils.isBlank(param)) {
			throw new AuthServiceException(RbaacServiceError.PARAM_INVALID, message);
		}
	}
}
