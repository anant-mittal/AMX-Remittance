package com.amx.jax.logger.events;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.logger.AuditEvent;
import com.amx.utils.EnumType;

public class SessionEvent extends AuditEvent {

	private static final long serialVersionUID = 6277691611931240782L;

	public static enum Type implements EnumType {
		SESSION_CREATED, SESSION_STARTED, SESSION_AUTHED, SESSION_EXPIRED, SESSION_UNAUTHED, SESSION_ENDED, SESSION_DESTROYED;
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
