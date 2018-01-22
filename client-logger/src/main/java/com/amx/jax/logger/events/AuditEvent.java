package com.amx.jax.logger.events;

public class AuditEvent {

	public static enum Type {
		DEFAULT_EVENT,
		//
		LOGIN_SUCCESS, LOGIN_FAIL;
		public static final Type DEFAULT = DEFAULT_EVENT;

		Type() {
		}
	}

	private Type type;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
