package com.amx.jax.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.meta.MetaData;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxAuditFilter implements AuditFilter<JaxAuditEvent> {

	@Autowired
	MetaData metaData;

	@Override
	public void doFilter(JaxAuditEvent event) {
		event.setCustomerId(metaData.getCustomerId());
	}
	
}
