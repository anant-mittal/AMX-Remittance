package com.amx.jax.model.response;

import java.math.BigDecimal;

public class ComponentDataDto {

private BigDecimal componentDataId;
private String componentDataDesc;
private String componentDataShortCode;
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
public String getComponentDataShortCode() {
	return componentDataShortCode;
}
public void setComponentDataShortCode(String componentDataShortCode) {
	this.componentDataShortCode = componentDataShortCode;
}
public ComponentDataDto() {
	super();
}
public ComponentDataDto(BigDecimal componentDataId, String componentDataDesc) {
	super();
	this.componentDataId = componentDataId;
	this.componentDataDesc = componentDataDesc;
}
public ComponentDataDto(BigDecimal componentDataId, String componentDataDesc,String componentDataShortCode) {
	super();
	this.componentDataId = componentDataId;
	this.componentDataDesc = componentDataDesc;
	this.componentDataShortCode= componentDataShortCode;
}

@Override
public String toString() {
	return "ComponentDataDto [componentDataId=" + componentDataId + ", componentDataDesc=" + componentDataDesc
			+ ", componentDataShortCode=" + componentDataShortCode + "]";
}


}
