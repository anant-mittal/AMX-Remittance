package com.amx.jax.logger;

import com.amx.jax.logger.events.AuditEvent;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface AuditLoggerService {

	public AuditEvent log(AuditEvent event) throws UnirestException;

}
