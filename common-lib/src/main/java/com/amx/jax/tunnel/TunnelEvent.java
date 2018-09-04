package com.amx.jax.tunnel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.stereotype.Component;

@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface TunnelEvent {

	TunnelEventScheme scheme() default TunnelEventScheme.ONCE_PER_COMPONENT;

	String topic();

	boolean integrity() default true;

	boolean queued() default false;
}