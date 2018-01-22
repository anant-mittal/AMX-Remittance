package com.amx.jax.logger.events;

public interface AuditLogEvent {

	public static enum Type {
		DEFAULT_EVENT,
		//
		LOGIN_SUCCESS, LOGIN_FAIL;
		public static final Type DEFAULT = DEFAULT_EVENT;

		Type() {
		}
	}

	abstract public long getTimestamp();

	abstract public Type getType();

	abstract public String getMessage();

	abstract public String getModuleName();

}
