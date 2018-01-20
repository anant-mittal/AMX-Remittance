package com.amx.jax.logger.events;

public enum AuditEventType {
	DEFAULT_EVENT,
	//
	LOGIN_SUCCESS, LOGIN_FAIL;

	public static final AuditEventType DEFAULT = DEFAULT_EVENT;

	AuditEventType() {
	}

}