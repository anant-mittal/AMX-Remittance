package com.amx.jax.logger.events;

import com.amx.jax.logger.AuditEvent;

import eu.bitwalker.useragentutils.UserAgent;

public class SessionEvent extends AuditEvent {

	private static final long serialVersionUID = 6277691611931240782L;

	public static enum Type implements EventType {
		SESSION_CREATED, SESSION_STARTED, SESSION_AUTHED, SESSION_EXPIRED, SESSION_UNAUTHED, SESSION_ENDED,
		SESSION_DESTROYED;

		@Override
		public EventMarker marker() {
			return EventMarker.GAUGE;
		}
	}

	UserAgent agent = null;
	String userKey = null;
	String sessionId = null;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public UserAgent getAgent() {
		return agent;
	}

	public void setDevice(UserAgent agent) {
		this.agent = agent;
	}

	public void setType(Type type) {
		super.setType(type);
	}

}
