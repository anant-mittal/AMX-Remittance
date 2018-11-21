package com.amx.jax.model.response;

import java.math.BigDecimal;

public class BranchSystemDetailDto {

	BigDecimal countryBranchSystemInventoryId;

	String systemName;

	String ipAddress;

	public BigDecimal getCountryBranchSystemInventoryId() {
		return countryBranchSystemInventoryId;
	}

	public void setCountryBranchSystemInventoryId(BigDecimal countryBranchSystemInventoryId) {
		this.countryBranchSystemInventoryId = countryBranchSystemInventoryId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
