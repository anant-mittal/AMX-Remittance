package com.amx.jax.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.meta.MetaData;

@Component
public class JaxAuditFilter implements AuditFilter<JaxTransactionEvent> {

	@Autowired
	MetaData metaData;

	@Override
	public void doFilter(JaxTransactionEvent event) {
		event.setCustomerId(metaData.getCustomerId());
	}
	
}
