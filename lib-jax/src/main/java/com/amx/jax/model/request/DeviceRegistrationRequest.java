package com.amx.jax.model.request;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.amx.jax.dict.UserClient.DeviceType;

public class DeviceRegistrationRequest {

	@NotNull
	@Size(min = 1, max = 40)
	String deviceId;

	@NotNull
	DeviceType deviceType;

	@NotNull
	@Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$", message = "Invalid branch system Ip")
	String branchSystemIp;

	@NotNull
	Integer countryBranchId;

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

	public Integer getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(Integer countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
}
