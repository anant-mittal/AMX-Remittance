package com.amx.amxlib.meta.model;

import java.math.BigDecimal;



public class ViewCityDto {
    private BigDecimal idNo;
	private BigDecimal cityMasterDescId;
	private BigDecimal languageId;
	private BigDecimal cityMasterId;
	private String cityName;
	private BigDecimal districtId;
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	public BigDecimal getCityMasterDescId() {
		return cityMasterDescId;
	}
	public void setCityMasterDescId(BigDecimal cityMasterDescId) {
		this.cityMasterDescId = cityMasterDescId;
	}
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	public BigDecimal getCityMasterId() {
		return cityMasterId;
	}
	public void setCityMasterId(BigDecimal cityMasterId) {
		this.cityMasterId = cityMasterId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public BigDecimal getDistrictId() {
		return districtId;
	}
	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}
}
