package com.amx.jax.auditlog.handlers;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.auditlogs.FieldListAuditEvent;
import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.utils.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FieldListAuditHandler extends AbstractAuditHanlder{

	@Override
	public JaxAuditEvent createAuditEvent() {
		DynamicFieldRequest model = (DynamicFieldRequest) JaxContextUtil.getRequestModel();
		JaxAuditEvent event = new FieldListAuditEvent(model);		
		return event;
	}

}