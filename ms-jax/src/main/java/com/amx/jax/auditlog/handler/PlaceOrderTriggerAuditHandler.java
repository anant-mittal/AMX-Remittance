package com.amx.jax.auditlog.handler;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.auditlog.JaxAuditEvent;
import com.amx.jax.auditlog.PlaceOrderTriggerAuditEvent;
import com.amx.jax.util.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class PlaceOrderTriggerAuditHandler extends AbstractAuditHanlder {

	@Override
	public JaxAuditEvent createAuditEvent() {
		Object model = JaxContextUtil.getRequestModel();
		JaxAuditEvent event = new PlaceOrderTriggerAuditEvent(model);		
		return event;
	}

}
