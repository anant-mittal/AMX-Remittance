package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class ServiceGroupMasterDescDto {

	private BigDecimal serviceGroupMasterId;
	private String serviceGroupDesc;
	private String serviceGroupShortDesc;

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

	public String getServiceGroupShortDesc() {
		return serviceGroupShortDesc;
	}

	public void setServiceGroupShortDesc(String serviceGroupShortDesc) {
		this.serviceGroupShortDesc = serviceGroupShortDesc;
	}
}
