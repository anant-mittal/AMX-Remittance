package com.amx.jax.tunnel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.stereotype.Component;

@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface TunnelEvent {
	Class<? extends Enum<?>> enumClass() default ITunnelEvents.class;

	String topic();

	boolean queued() default false;
}