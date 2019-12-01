package com.amx.jax.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.amx.utils.Constants;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequest {

	RequestType type() default RequestType.DEFAULT;

	boolean useAuthKey() default false;

	boolean useAuthToken() default false;

	String flow() default Constants.BLANK;

	String feature() default Constants.BLANK;

}