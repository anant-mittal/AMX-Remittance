package com.amx.jax.sso;

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

	String terminalId;
	String terminalIp;
	String deviceId;
	String deviceRegId;
	String identity;
	ClientType clientType;

	public SSOAuditEvent(Type eventType) {
		super(eventType);
	}

	public SSOAuditEvent(Type eventType, Result result) {
		super(eventType, result);
	}

	public SSOAuditEvent deviceId(Object deviceId) {
		this.deviceId = ArgUtil.parseAsString(deviceId);
		return this;
	}

	public SSOAuditEvent identity(Object identity) {
		this.identity = ArgUtil.parseAsString(identity);
		return this;
	}

	public SSOAuditEvent terminalId(Object terminalId) {
		this.terminalId = ArgUtil.parseAsString(terminalId);
		return this;
	}

	public SSOAuditEvent terminalIp(Object terminalIp) {
		this.terminalIp = ArgUtil.parseAsString(terminalIp);
		return this;
	}

	public SSOAuditEvent deviceRegId(Object deviceRegId) {
		this.deviceRegId = ArgUtil.parseAsString(deviceRegId);
		return this;
	}

	public SSOAuditEvent clientType(ClientType clientType) {
		this.clientType = clientType;
		return this;
	}

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
}
