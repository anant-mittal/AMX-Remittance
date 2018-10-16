package com.amx.jax.model.request;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class DynamicFieldRequest {
	
	@ApiModelProperty(example="KWT")
	private String tenant;
	
	@ApiModelProperty(example="EGYPT")
	private String nationality;
	
	@ApiModelProperty(example="REGISTRATION")
	private String component;
	
	@ApiModelProperty(example="198")
	private BigDecimal componentDataId;
	
	@ApiModelProperty(example="CIVIL ID")
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
