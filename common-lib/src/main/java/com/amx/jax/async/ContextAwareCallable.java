package com.amx.jax.async;

import java.util.concurrent.Callable;

import org.slf4j.Logger;

import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.LoggerService;

public class ContextAwareCallable<T> implements Callable<T> {

	Logger LOGGER = LoggerService.getLogger(ContextAwareCallable.class);

	private Callable<T> task;
	private AppContext context = null;

	public ContextAwareCallable(Callable<T> task, AppContext context) {
		this.task = task;
		this.context = context;
	}

	@Override
	public T call() throws Exception {
		if (context != null) {
			AppContextUtil.setContext(context);
			AppContextUtil.init();
		}
		try {
			return task.call();
		} catch (Exception e) {
			LOGGER.error("task.call", e);
			throw new Exception(e);
		} finally {
			AppContextUtil.clear();
		}
	}
}
