package com.amx.jax.async;

import java.util.concurrent.Callable;

import org.apache.log4j.MDC;

import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ContextUtil;

public class ContextAwareCallable<T> implements Callable<T> {
	private Callable<T> task;
	AppContext context;

	public ContextAwareCallable(Callable<T> task, AppContext context) {
		this.context = context;
	}

	@Override
	public T call() throws Exception {
		if (context != null) {
			AppContextUtil.setContext(context);
			MDC.put(ContextUtil.TRACE_ID, context.getTraceId());
			MDC.put(TenantContextHolder.TENANT, context.getTenant());
		}
		try {
			return task.call();
		} finally {
			MDC.clear();
			ContextUtil.clear();
		}
	}
}