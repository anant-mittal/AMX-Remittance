package com.amx.jax.auditlog.handlers;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.auditlogs.DistrictListAuditEvent;
import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.model.request.CommonRequest;
import com.amx.jax.utils.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class DistrictListAuditHandler extends AbstractAuditHanlder {
	@Override
	public JaxAuditEvent createAuditEvent() {
		CommonRequest model = (CommonRequest) JaxContextUtil.getRequestModel();
		JaxAuditEvent event = new DistrictListAuditEvent(model);		
		return event;
	}

}
