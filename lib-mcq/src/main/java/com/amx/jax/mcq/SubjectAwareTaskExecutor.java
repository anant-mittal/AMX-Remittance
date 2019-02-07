package com.amx.jax.mcq;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SubjectAwareTaskExecutor extends ScheduledThreadPoolExecutor {

	private static final long serialVersionUID = 1822934135306438862L;

	public SubjectAwareTaskExecutor(int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	public void execute(final Runnable aTask) {
		super.execute(aTask);
	}
}
