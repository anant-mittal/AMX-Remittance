package com.amx.jax.logger.events;

import com.amx.jax.logger.AuditEvent;

public class DefaultEvent extends AuditEvent {

	public static enum Type implements EventType {
		DEFAULT_EVENT;
		public static final EventType DEFAULT = DEFAULT_EVENT;
	}

}
