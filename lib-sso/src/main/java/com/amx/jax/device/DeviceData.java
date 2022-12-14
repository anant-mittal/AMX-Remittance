package com.amx.jax.device;

import java.io.Serializable;

import com.amx.jax.dict.UserClient.ClientType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceData implements Serializable {
	private static final long serialVersionUID = 2981932845270868040L;
	private String regId;
	private String terminalId;
	private String empId;
	private String globalIp;
	private String localIp;
	private ClientType clientType;

	private long updatestamp;

	private long checkstamp;

	public long getUpdatestamp() {
		return updatestamp;
	}

	public void setUpdatestamp(long updatestamp) {
		this.updatestamp = updatestamp;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	private String deviceReqKey;
	private String sessionPairingTokenX;

	public String getSessionPairingTokenX() {
		return sessionPairingTokenX;
	}

	public void setSessionPairingTokenX(String sessionPairingTokenX) {
		this.sessionPairingTokenX = sessionPairingTokenX;
	}

	public String getDeviceReqKey() {
		return deviceReqKey;
	}

	public void setDeviceReqKey(String deviceReqKey) {
		this.deviceReqKey = deviceReqKey;
	}

	public String getGlobalIp() {
		return globalIp;
	}

	public void setGlobalIp(String globalIp) {
		this.globalIp = globalIp;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public long getCheckstamp() {
		return checkstamp;
	}

	public void setCheckstamp(long checkstamp) {
		this.checkstamp = checkstamp;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
}