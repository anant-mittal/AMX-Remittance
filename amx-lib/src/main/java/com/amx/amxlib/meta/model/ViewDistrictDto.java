package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
/*
 * Author : Rabil
 * Date   :22 Nov 2017
 */


public class ViewDistrictDto {
	
	
	/**
	 * 
	 */



	public BigDecimal stateId;
	public BigDecimal districtId;
	public String districtDesc;

	public BigDecimal languageId;
	public BigDecimal getStateId() {
		return stateId;
	}
	public void setStateId(BigDecimal stateId) {
		this.stateId = stateId;
	}
	public BigDecimal getDistrictId() {
		return districtId;
	}
	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}
	public String getDistrictDesc() {
		return districtDesc;
	}
	public void setDistrictDesc(String districtDesc) {
		this.districtDesc = districtDesc;
	}
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	
	

}
