package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class ServiceGroupMasterDescDto {

	private BigDecimal serviceGroupMasterId;
	private String serviceGroupDesc;

	public BigDecimal getServiceGroupMasterId() {
		return serviceGroupMasterId;
	}

	public void setServiceGroupMasterId(BigDecimal serviceGroupMasterId) {
		this.serviceGroupMasterId = serviceGroupMasterId;
	}

	public String getServiceGroupDesc() {
		return serviceGroupDesc;
	}

	public void setServiceGroupDesc(String serviceGroupDesc) {
		this.serviceGroupDesc = serviceGroupDesc;
	}
}
