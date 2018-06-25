package com.amx.jax.logger.events;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.logger.AuditEvent;

public class SessionEvent extends AuditEvent {

	private static final long serialVersionUID = 6277691611931240782L;

	public static enum Type implements EventType {
		SESSION_CREATED, SESSION_STARTED, SESSION_AUTHED, SESSION_EXPIRED, SESSION_UNAUTHED, SESSION_ENDED, SESSION_DESTROYED;

		@Override
		public EventMarker marker() {
			if (this == SESSION_AUTHED || this == SESSION_EXPIRED || this == SESSION_UNAUTHED) {
				return EventMarker.AUDIT;
			}
			return EventMarker.GAUGE;
		}
	}

	Map<String, Object> device = new HashMap<String, Object>();
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

	public Map<String, Object> getDevice() {
		return device;
	}

	public void setDevice(Map<String, Object> device) {
		this.device = device;
	}

	public void setType(Type type) {
		super.setType(type);
	}

}
