package com.amx.jax.logger;

import com.amx.jax.logger.events.SessionEvent;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface AuditLoggerService {

	public AuditLoggerResponse log(SessionEvent event) throws UnirestException;

}
