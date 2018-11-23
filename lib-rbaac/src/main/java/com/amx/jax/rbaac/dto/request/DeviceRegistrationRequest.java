package com.amx.jax.rbaac.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.amx.jax.dict.UserClient.ClientType;

public class DeviceRegistrationRequest {

	@NotNull
	@Size(min = 1, max = 40)
	String deviceId;

	@NotNull
	ClientType deviceType;

	@Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$", message = "Invalid branch system Ip")
	String branchSystemIp;
	
	String identityInt;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getBranchSystemIp() {
		return branchSystemIp;
	}

	public void setBranchSystemIp(String branchSystemIp) {
		this.branchSystemIp = branchSystemIp;
	}

	public ClientType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(ClientType deviceType) {
		this.deviceType = deviceType;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

}
