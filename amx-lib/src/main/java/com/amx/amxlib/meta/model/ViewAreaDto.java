package com.amx.amxlib.meta.model;

import java.math.BigDecimal;


public class ViewAreaDto {


	private BigDecimal areaCode;
	private String areaDesc;
	private String shortDesc;
	public BigDecimal getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(BigDecimal areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaDesc() {
		return areaDesc;
	}
	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	
}
