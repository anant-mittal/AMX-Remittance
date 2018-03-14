package com.amx.jax.logger.events;

public class AuthEvent extends DefaultEvent {

	public enum AuthEventType implements EventType {
		LOGIN_ATTEMPT, LOGIN_SUCCESS, LOGIN_FAIL;
	}
}
