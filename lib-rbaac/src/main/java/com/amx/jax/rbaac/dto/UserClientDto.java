package com.amx.jax.rbaac.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.DeviceType;

public class UserClientDto implements Serializable {

	private static final long serialVersionUID = -6211325337812824961L;

	// Category Informations
	ClientType clientType;

	@NotNull(message = "Device Type Can not be Null or Empty")
	DeviceType deviceType;

	// Terminal Identifications
	BigDecimal terminalId;

	@NotBlank(message = "Local Ip Address Can not be Null or Empty")
	String localIpAddress;

	@NotBlank(message = "Global Ip Address Can not be Null or Empty")
	String globalIpAddress;

	// Device Identifications
	String deviceId; // req
	BigDecimal deviceRegId; // req
	String deviceRegToken; // req

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

	public BigDecimal getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(BigDecimal terminalId) {
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

	public BigDecimal getDeviceRegId() {
		return deviceRegId;
	}

	public void setDeviceRegId(BigDecimal deviceRegId) {
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

	@Override
	public String toString() {
		return "UserClientDto [clientType=" + clientType + ", deviceType=" + deviceType + ", terminalId=" + terminalId
				+ ", localIpAddress=" + localIpAddress + ", globalIpAddress=" + globalIpAddress + ", deviceId="
				+ deviceId + ", deviceRegId=" + deviceRegId + ", deviceRegToken=" + deviceRegToken
				+ ", deviceSessionToken=" + deviceSessionToken + ", deviceRequestToken=" + deviceRequestToken + "]";
	}
	
	
	

}
