package com.amx.jax.tunnel;

import org.springframework.stereotype.Component;

@Component
public @interface TunnelEvent {
	String topic() default "topic";
}