package com.amx.jax.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.amx.utils.Constants;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequest {

	public enum ResponeError {
		OK, PROPAGATE, SUPPRESS, DEFAULT
	}

	RequestType type() default RequestType.DEFAULT;

	String deprecated() default Constants.BLANK;

	boolean useAuthKey() default false;

	boolean useAuthToken() default false;

	String flow() default Constants.BLANK;

	String feature() default Constants.BLANK;
	
	String tracefilter() default Constants.BLANK;

	String clientAuth() default Constants.BLANK;

	ResponeError responeError() default ResponeError.DEFAULT;

}