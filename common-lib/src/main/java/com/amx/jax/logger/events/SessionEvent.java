package com.amx.jax.logger.events;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.model.UserDevice;

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

	UserDevice device = new UserDevice();;
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

	public UserDevice getDevice() {
		return device;
	}

	public void setDevice(UserDevice device) {
		this.device = device;
	}

	public void setType(Type type) {
		super.setType(type);
	}

}
