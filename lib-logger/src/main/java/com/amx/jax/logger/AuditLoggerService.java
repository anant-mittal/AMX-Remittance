package com.amx.jax.logger;

import com.amx.jax.logger.events.SessionEvent;

public interface AuditLoggerService {

	public AuditLoggerResponse log(SessionEvent event);

}
