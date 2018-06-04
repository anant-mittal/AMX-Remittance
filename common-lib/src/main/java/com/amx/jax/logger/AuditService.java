package com.amx.jax.logger;

public interface AuditService {

	public AuditLoggerResponse log(AuditEvent event);

	public AuditLoggerResponse track(AuditEvent event);

	public AuditLoggerResponse gauge(AuditEvent event);

	public AuditLoggerResponse log(AuditEvent event, Exception e);

	public AuditLoggerResponse track(AuditEvent event, Exception e);

	public AuditLoggerResponse gauge(AuditEvent event, Exception e);

}
