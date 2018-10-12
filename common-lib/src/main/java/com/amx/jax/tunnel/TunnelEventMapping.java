package com.amx.jax.tunnel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.async.ExecutorConfig;
import com.amx.utils.Constants;

@Component
@Async(ExecutorConfig.EXECUTER_WORKER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TunnelEventMapping {

	TunnelEventXchange scheme() default TunnelEventXchange.SHOUT_LISTNER;

	String topic() default Constants.BLANK;

	Class<?> byEvent() default ITunnelEvent.class;

	boolean integrity() default true;

	boolean queued() default false;
}
