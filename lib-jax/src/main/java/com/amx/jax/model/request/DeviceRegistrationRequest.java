package com.amx.jax.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DeviceRegistrationRequest {

	@NotNull
	@Size(min = 1, max = 40)
	String deviceId;

	@NotNull
	@Size(min = 1, max = 20)
	String deviceType;

	@NotNull
	@Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$", message = "Invalid branch system Ip")
	String branchSystemIp;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getBranchSystemIp() {
		return branchSystemIp;
	}

	public void setBranchSystemIp(String branchSystemIp) {
		this.branchSystemIp = branchSystemIp;
	}

}
