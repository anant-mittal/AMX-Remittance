package com.amx.jax.auditlog.handlers;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.auditlogs.SendOtpAuditEvent;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.utils.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class SendOtpAuditHandler extends AbstractAuditHanlder {

	@Override
	public JaxAuditEvent createAuditEvent() {
		OffsiteCustomerRegistrationRequest model = (OffsiteCustomerRegistrationRequest) JaxContextUtil.getRequestModel();
		JaxAuditEvent event = new SendOtpAuditEvent(model);		
		return event;
	}}
