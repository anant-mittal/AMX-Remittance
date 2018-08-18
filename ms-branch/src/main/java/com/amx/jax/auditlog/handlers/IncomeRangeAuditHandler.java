package com.amx.jax.auditlog.handlers;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.request.EmploymentDetailsRequest;
import com.amx.jax.auditlogs.IncomeRangeAuditEvent;
import com.amx.jax.auditlogs.JaxAuditEvent;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class IncomeRangeAuditHandler extends AbstractAuditHanlder{

	@Override
	public JaxAuditEvent createAuditEvent() {
		EmploymentDetailsRequest model = new EmploymentDetailsRequest();
		JaxAuditEvent event = new IncomeRangeAuditEvent(model);		
		return event;
	}

}
