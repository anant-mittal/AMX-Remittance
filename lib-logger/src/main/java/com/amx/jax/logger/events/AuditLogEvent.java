package com.amx.jax.logger.events;

public interface AuditLogEvent {

	public static enum EventType {
		DEFAULT_EVENT,
		//
		LOGIN_SUCCESS, LOGIN_FAIL;
		public static final EventType DEFAULT = DEFAULT_EVENT;

		EventType() {
		}
	}

	abstract public long getTimestamp();

	abstract public EventType getType();

	abstract public String getMessage();

	abstract public String getModuleName();

}
