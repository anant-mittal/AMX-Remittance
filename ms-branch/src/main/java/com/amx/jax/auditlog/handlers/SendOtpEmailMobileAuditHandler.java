package com.amx.jax.auditlog.handlers;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.auditlogs.SendOtpEmailMobileAuditEvent;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.utils.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class SendOtpEmailMobileAuditHandler extends AbstractAuditHanlder
{

	@Override
	public JaxAuditEvent createAuditEvent() {
		CustomerPersonalDetail customerPersonalDetails = (CustomerPersonalDetail) JaxContextUtil.getRequestModel();
		JaxAuditEvent event = new SendOtpEmailMobileAuditEvent(customerPersonalDetails);		
		return event;
	}

}
