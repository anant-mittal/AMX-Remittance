package com.amx.jax.logger;

import com.amx.jax.logger.events.AbstractAuditEvent;

public interface AuditService {

	public AuditLoggerResponse logRest(AbstractAuditEvent event);

	public AuditLoggerResponse log(AbstractAuditEvent event);

}
