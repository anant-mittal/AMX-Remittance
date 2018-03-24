package com.amx.jax.logger;

public interface AuditService {

	public AuditLoggerResponse log(AuditEvent event);

}
