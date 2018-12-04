package com.amx.jax.rbaac.dto;

import java.io.Serializable;

import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.DeviceType;

public class UserClientDto implements Serializable {

	private static final long serialVersionUID = -6211325337812824961L;

	// Category Informations
	ClientType clientType;
	DeviceType deviceType;

	// Terminal Identifications
	String terminalId;
	String localIpAddress;
	String globalIpAddress;

	// Device Identifications
	String deviceId;
	String deviceRegId;
	String deviceRegToken;

	// Session/Request Identifications
	String deviceSessionToken;
	String deviceRequestToken;

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getLocalIpAddress() {
		return localIpAddress;
	}

	public void setLocalIpAddress(String localIpAddress) {
		this.localIpAddress = localIpAddress;
	}

	public String getGlobalIpAddress() {
		return globalIpAddress;
	}

	public void setGlobalIpAddress(String globalIpAddress) {
		this.globalIpAddress = globalIpAddress;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceRegId() {
		return deviceRegId;
	}

	public void setDeviceRegId(String deviceRegId) {
		this.deviceRegId = deviceRegId;
	}

	public String getDeviceRegToken() {
		return deviceRegToken;
	}

	public void setDeviceRegToken(String deviceRegToken) {
		this.deviceRegToken = deviceRegToken;
	}

	public String getDeviceSessionToken() {
		return deviceSessionToken;
	}

	public void setDeviceSessionToken(String deviceSessionToken) {
		this.deviceSessionToken = deviceSessionToken;
	}

	public String getDeviceRequestToken() {
		return deviceRequestToken;
	}

	public void setDeviceRequestToken(String deviceRequestToken) {
		this.deviceRequestToken = deviceRequestToken;
	}

}
