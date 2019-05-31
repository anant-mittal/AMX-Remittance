package com.amx.jax.sso;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.events.AuditActorInfo;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SSOAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 3330563732707068857L;

	public static enum Type implements EventType {
		DEVICE_PAIR, DEVICE_SESSION_CREATED, DEVICE_SESSION_PAIR, SESSION_TERMINAL_MAP,
		CARD_SCANNED,
		LOGIN_INIT, LOGIN_OTP, ACTIVE;
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
		String deviceRegId;
		String identity;
		String empno;

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

		public String getIdentity() {
			return identity;
		}

		public void setIdentity(String identity) {
			this.identity = identity;
		}

		public String getEmpno() {
			return empno;
		}

		public void setEmpno(String empno) {
			this.empno = empno;
		}
	}

	private SSOAuditData auth;
	private AuditActorInfo actor;

	public SSOAuditEvent(Type eventType) {
		super(eventType);
		this.auth = new SSOAuditData();
		this.actor = new AuditActorInfo();
	}

	public SSOAuditEvent(Type eventType, Result result) {
		super(eventType, result);
		this.auth = new SSOAuditData();
		this.actor = new AuditActorInfo();
	}

	public SSOAuditEvent identity(Object identity) {
		this.auth.setIdentity(ArgUtil.parseAsString(identity));
		this.actor.setIdentity(ArgUtil.parseAsString(identity));
		return this;
	}

	public SSOAuditEvent empno(Object empno) {
		this.auth.setEmpno(ArgUtil.parseAsString(empno));
		this.actor.setEmpno(ArgUtil.parseAsString(empno));
		return this;
	}

	public SSOAuditEvent terminalId(Object terminalId) {
		this.auth.setTerminalId(ArgUtil.parseAsString(terminalId));
		this.actor.setTerminalId(ArgUtil.parseAsString(terminalId));
		return this;
	}

	public SSOAuditEvent terminalIp(Object terminalIp) {
		this.auth.setTerminalIp(ArgUtil.parseAsString(terminalIp));
		this.actor.setTerminalIp(ArgUtil.parseAsString(terminalIp));
		return this;
	}

	public SSOAuditEvent deviceRegId(Object deviceRegId) {
		this.auth.setDeviceRegId(ArgUtil.parseAsString(deviceRegId));
		this.actor.setDeviceRegId(ArgUtil.parseAsString(deviceRegId));
		return this;
	}

	public SSOAuditEvent clientType(ClientType clientType) {
		AppContextUtil.getUserClient().setClientType(clientType);
		return this;
	}

	public SSOAuditEvent branchId(BigDecimal branchId) {
		this.actor.setBranchId(branchId);
		return this;
	}

	public SSOAuditEvent branchUser(String username) {
		this.actor.setUsername(username);
		return this;
	}

	public SSOAuditEvent branchName(String branchName) {
		this.actor.setBranchName(branchName);
		return this;
	}

	public SSOAuditEvent branchArea(String branchArea) {
		this.actor.setAreaName(branchArea);
		return this;
	}

	public SSOAuditData getAuth() {
		return auth;
	}

	public void setAuth(SSOAuditData auth) {
		this.auth = auth;
	}

}
