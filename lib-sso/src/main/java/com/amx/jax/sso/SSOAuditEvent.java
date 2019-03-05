package com.amx.jax.sso;

import java.io.Serializable;

import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.logger.AuditEvent;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SSOAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 3330563732707068857L;

	public static enum Type implements EventType {
		DEVICE_PAIR, DEVICE_SESSION_CREATED, DEVICE_SESSION_PAIR, SESSION_TERMINAL_MAP, CARD_SCANNED,
		LOGIN_INIT, LOGIN_OTP

		;
		@Override
		public EventMarker marker() {
			return EventMarker.NOTICE;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SSOAuditData implements Serializable {
		private static final long serialVersionUID = -1765231313729431488L;
		String terminalId;
		String terminalIp;
		String deviceId;
		String deviceRegId;
		String identity;
		ClientType clientType;

		public String getTerminalId() {
			return terminalId;
		}

		public void setTerminalId(String terminalId) {
			this.terminalId = terminalId;
		}

		public String getTerminalIp() {
			return terminalIp;
		}

		public void setTerminalIp(String terminalIp) {
			this.terminalIp = terminalIp;
		}

		public String getDeviceRegId() {
			return deviceRegId;
		}

		public void setDeviceRegId(String deviceRegId) {
			this.deviceRegId = deviceRegId;
		}

		public ClientType getClientType() {
			return clientType;
		}

		public void setClientType(ClientType clientType) {
			this.clientType = clientType;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getIdentity() {
			return identity;
		}

		public void setIdentity(String identity) {
			this.identity = identity;
		}
	}

	private SSOAuditData auth;

	public SSOAuditEvent(Type eventType) {
		super(eventType);
		this.auth = new SSOAuditData();
	}

	public SSOAuditEvent(Type eventType, Result result) {
		super(eventType, result);
		this.auth = new SSOAuditData();
	}

	public SSOAuditEvent deviceId(Object deviceId) {
		this.auth.setDeviceId(ArgUtil.parseAsString(deviceId));
		return this;
	}

	public SSOAuditEvent identity(Object identity) {
		this.auth.setIdentity(ArgUtil.parseAsString(identity));
		return this;
	}

	public SSOAuditEvent terminalId(Object terminalId) {
		this.auth.setTerminalId(ArgUtil.parseAsString(terminalId));
		return this;
	}

	public SSOAuditEvent terminalIp(Object terminalIp) {
		this.auth.setTerminalIp(ArgUtil.parseAsString(terminalIp));
		return this;
	}

	public SSOAuditEvent deviceRegId(Object deviceRegId) {
		this.auth.setDeviceRegId(ArgUtil.parseAsString(deviceRegId));
		return this;
	}

	public SSOAuditEvent clientType(ClientType clientType) {
		this.auth.setClientType(clientType);
		return this;
	}

}
