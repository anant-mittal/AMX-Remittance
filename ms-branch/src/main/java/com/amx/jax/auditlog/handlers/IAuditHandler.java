package com.amx.jax.auditlog.handlers;

import com.amx.jax.auditlogs.JaxAuditEvent;

public interface IAuditHandler {

	JaxAuditEvent createAuditEvent();
}
