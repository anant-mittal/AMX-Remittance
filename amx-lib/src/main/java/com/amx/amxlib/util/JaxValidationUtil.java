/**
 * 
 */
package com.amx.amxlib.util;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.error.JaxError;

/**
 * utility for validtion of model classes
 * 
 * @author Prashant
 *
 */
public class JaxValidationUtil {

	public static void validatePositiveNumber(Number num, String errorMessage, JaxError jaxError) {
		if (num != null && num.doubleValue() <= 0) {
			throw new GlobalException(jaxError, errorMessage);
		}
	}

	public static void validatePositiveNumber(Number num, String errorMessage) {
		validatePositiveNumber(num, errorMessage, JaxError.INVALID_NUMBER);
	}

	public static void validatePositiveNumber(Number num) {
		validatePositiveNumber(num, "Number is not positive", JaxError.INVALID_NUMBER);
	}
}
