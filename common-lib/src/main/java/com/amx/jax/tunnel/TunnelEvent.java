package com.amx.jax.tunnel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.async.ExecutorConfig;

@Component
@Async(ExecutorConfig.EXECUTER_WORKER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TunnelEvent {

	TunnelEventXchange scheme() default TunnelEventXchange.SHOUT_LISTNER;

	String topic();

	boolean integrity() default true;

	boolean queued() default false;
}