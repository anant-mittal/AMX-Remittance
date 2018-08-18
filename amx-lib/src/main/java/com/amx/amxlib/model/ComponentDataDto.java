package com.amx.amxlib.model;

import java.math.BigDecimal;

public class ComponentDataDto {
private BigDecimal componentDataId;
private String componentDataDesc;
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

public ComponentDataDto(BigDecimal componentDataId, String componentDataDesc) {
	super();
	this.componentDataId = componentDataId;
	this.componentDataDesc = componentDataDesc;
}
@Override
public String toString() {
	return "ComponentDataDto [componentDataId=" + componentDataId + ", componentDataDesc=" + componentDataDesc + "]";
}


}
