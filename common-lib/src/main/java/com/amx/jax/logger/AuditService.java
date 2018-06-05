package com.amx.jax.logger;

import org.slf4j.Logger;

public interface AuditService {

	public AuditLoggerResponse log(AuditEvent event);

	public AuditLoggerResponse track(AuditEvent event);

	public AuditLoggerResponse gauge(AuditEvent event);

	public AuditLoggerResponse log(AuditEvent event, Exception e);

	public AuditLoggerResponse track(AuditEvent event, Exception e);

	public AuditLoggerResponse gauge(AuditEvent event, Exception e);

	public AuditLoggerResponse excep(AuditEvent event, Exception e);

	public AuditLoggerResponse excep(AuditEvent event, Logger logger, Exception e);

}
