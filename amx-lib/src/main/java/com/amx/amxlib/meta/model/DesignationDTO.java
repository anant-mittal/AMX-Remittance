package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class DesignationDTO {
	
	public BigDecimal civilId;
	
	public String designation;
	
	public BigDecimal getCivilId() {
		return civilId;
	}
	public void setCivilId(BigDecimal civilId) {
		this.civilId = civilId;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
}
