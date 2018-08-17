package com.amx.amxlib.model;

import java.math.BigDecimal;


public class BizComponentDataDescDto {
	
	private BigDecimal componentDataDescId;
	private BigDecimal fsBizComponentData;
	private BigDecimal fsLanguageType;
	private String dataDesc;
	public BigDecimal getComponentDataDescId() {
		return componentDataDescId;
	}
	public void setComponentDataDescId(BigDecimal componentDataDescId) {
		this.componentDataDescId = componentDataDescId;
	}
	public BigDecimal getFsBizComponentData() {
		return fsBizComponentData;
	}
	public void setFsBizComponentData(BigDecimal fsBizComponentData) {
		this.fsBizComponentData = fsBizComponentData;
	}
	public BigDecimal getFsLanguageType() {
		return fsLanguageType;
	}
	public void setFsLanguageType(BigDecimal fsLanguageType) {
		this.fsLanguageType = fsLanguageType;
	}
	public String getDataDesc() {
		return dataDesc;
	}
	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}
	
	 

}
