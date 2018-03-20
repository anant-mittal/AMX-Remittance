package com.amx.jax.logger.client;

import com.amx.jax.logger.AuditEvent;

public interface AuditFilter<T extends AuditEvent> {
	public void doFilter(T event);
}
