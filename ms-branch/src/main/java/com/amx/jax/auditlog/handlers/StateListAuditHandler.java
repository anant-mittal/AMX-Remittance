package com.amx.jax.auditlog.handlers;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import com.amx.amxlib.model.request.CommonRequest;
import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.auditlogs.StateListAuditEvent;
import com.amx.jax.utils.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class StateListAuditHandler extends AbstractAuditHanlder {

	@Override
	public JaxAuditEvent createAuditEvent() {
		CommonRequest model = (CommonRequest) JaxContextUtil.getRequestModel();
		JaxAuditEvent event = new StateListAuditEvent(model);		
		return event;
	}

}
