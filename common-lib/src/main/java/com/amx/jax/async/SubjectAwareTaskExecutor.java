package com.amx.jax.async;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class SubjectAwareTaskExecutor extends ThreadPoolTaskExecutor {
	private static final long serialVersionUID = 1822934135306438862L;

	@Override
	public void execute(final Runnable aTask) {
		super.execute(aTask);
	}
}
