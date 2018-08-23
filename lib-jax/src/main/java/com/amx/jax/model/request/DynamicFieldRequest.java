package com.amx.jax.model.request;

public class DynamicFieldRequest {
	private String tenant;
	private String nationality;
	private String component;

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

	@Override
	public String toString() {
		return "DynamicFieldRequest [tenant=" + tenant + ", nationality=" + nationality + ", component=" + component
				+ "]";
	}

}
