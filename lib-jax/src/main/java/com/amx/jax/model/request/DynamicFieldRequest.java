package com.amx.jax.model.request;

import java.math.BigDecimal;

public class DynamicFieldRequest {
	private String tenant;
	private String nationality;
	private String component;
	private BigDecimal componentDataId;
	private String componentDataDesc;

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public BigDecimal getComponentDataId() {
		return componentDataId;
	}

	public void setComponentDataId(BigDecimal componentDataId) {
		this.componentDataId = componentDataId;
	}

	public String getComponentDataDesc() {
		return componentDataDesc;
	}

	public void setComponentDataDesc(String componentDataDesc) {
		this.componentDataDesc = componentDataDesc;
	}

	@Override
	public String toString() {
		return "DynamicFieldRequest [tenant=" + tenant + ", nationality=" + nationality + ", component=" + component +", componentDataId =" + componentDataId + ", componentDataDesc=" + componentDataDesc + "]";
	}

}
