package com.amx.jax;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * @author lalittanwar
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AppParamKey {
	AppParam value();
}