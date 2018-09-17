package com.amx.jax.model.request;

public class DynamicFieldRequest {
	private String tenant;
	private String nationality;
	private String component;
	private String componentDataId;

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

	public String getComponentDataId() {
		return componentDataId;
	}

	public void setComponentDataId(String componentDataId) {
		this.componentDataId = componentDataId;
	}

	@Override
	public String toString() {
		return "DynamicFieldRequest [tenant=" + tenant + ", nationality=" + nationality + ", component=" + component +", componentDataId=" + componentDataId + "]";
	}

}
