package com.amx.jax.device;

import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.logger.AuditEvent;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 3330563732707068857L;

	public static enum Type implements EventType {
		DEVICE_PAIR, DEVICE_SESSION, SESSION_PAIR, SESSION_TERMINAL, CARD_SCANNED

		;
		@Override
		public EventMarker marker() {
			return EventMarker.NOTICE;
		}
	}

	String terminalId;
	String terminalIp;
	String deviceRegId;
	String identity;
	ClientType clientType;

	public DeviceAuditEvent(Type eventType) {
		super(eventType);
	}

	public DeviceAuditEvent identity(Object identity) {
		this.identity = ArgUtil.parseAsString(identity);
		return this;
	}

	public DeviceAuditEvent terminalId(Object terminalId) {
		this.terminalId = ArgUtil.parseAsString(terminalId);
		return this;
	}

	public DeviceAuditEvent terminalIp(Object terminalIp) {
		this.terminalIp = ArgUtil.parseAsString(terminalIp);
		return this;
	}

	public DeviceAuditEvent deviceRegId(Object deviceRegId) {
		this.deviceRegId = ArgUtil.parseAsString(deviceRegId);
		return this;
	}

	public DeviceAuditEvent clientType(ClientType clientType) {
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
}
