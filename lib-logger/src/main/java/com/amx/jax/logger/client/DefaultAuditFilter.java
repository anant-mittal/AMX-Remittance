package com.amx.jax.logger.client;

import org.springframework.stereotype.Component;

import com.amx.jax.logger.events.DefaultEvent;

@Component
public class DefaultAuditFilter implements AuditFilter<DefaultEvent> {

	@Override
	public void doFilter(DefaultEvent event) {
		// Do Something
	}

}
