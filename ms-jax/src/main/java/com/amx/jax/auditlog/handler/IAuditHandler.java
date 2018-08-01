package com.amx.jax.auditlog.handler;

import com.amx.jax.auditlog.JaxAuditEvent;

public interface IAuditHandler {

	JaxAuditEvent createAuditEvent();
}
