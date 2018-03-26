package com.amx.jax.async;

import java.util.concurrent.Callable;

import org.apache.log4j.MDC;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ContextUtil;

public class ContextAwareCallable<T> implements Callable<T> {
	private Callable<T> task;
	private Tenant tnt;
	private String traceId;

	public ContextAwareCallable(Callable<T> task, Tenant tnt, String traceId) {
		this.task = task;
		this.traceId = traceId;
		this.tnt = tnt;
	}

	@Override
	public T call() throws Exception {
		if (traceId != null) {
			ContextUtil.setTraceId(traceId);
		}
		if (tnt != null) {
			TenantContextHolder.setCurrent(tnt);
		}
		MDC.put(ContextUtil.TRACE_ID, traceId);
		MDC.put(TenantContextHolder.TENANT, tnt);
		try {
			return task.call();
		} finally {
			MDC.clear();
			ContextUtil.clear();
		}
	}
}