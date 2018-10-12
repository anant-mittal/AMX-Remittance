package com.amx.jax.sample;

import org.springframework.stereotype.Component;

import com.amx.jax.logger.client.AuditFilter;

@Component
public class DefaultAuditFilter implements AuditFilter<DefaultEvent> {

	@Override
	public void doFilter(DefaultEvent event) {
		
	}


}
