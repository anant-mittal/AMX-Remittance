package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class ViewGovernateDto {

	private BigDecimal governateId;
	private String fullName;
	private String arFullName;
	private BigDecimal applicationCountryId;
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getArFullName() {
		return arFullName;
	}
	public void setArFullName(String arFullName) {
		this.arFullName = arFullName;
	}
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public BigDecimal getGovernateId() {
		return governateId;
	}
	public void setGovernateId(BigDecimal governateId) {
		this.governateId = governateId;
	}
}
