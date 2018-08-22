package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class CommonRequest {
private BigDecimal countryId;
private BigDecimal stateId;
private BigDecimal districtId;
private BigDecimal cityId;
private BigDecimal nationalityId;	

public BigDecimal getCountryId() {
	return countryId;
}
public void setCountryId(BigDecimal countryId) {
	this.countryId = countryId;
}
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
public BigDecimal getCityId() {
	return cityId;
}
public void setCityId(BigDecimal cityId) {
	this.cityId = cityId;
}
public BigDecimal getNationalityId() {
	return nationalityId;
}
public void setNationalityId(BigDecimal nationalityId) {
	this.nationalityId = nationalityId;
}
@Override
public String toString() {
	return "CommonRequest [countryId=" + countryId + ", stateId=" + stateId + ", districtId=" + districtId + ", cityId="
			+ cityId + ", nationalityId=" + nationalityId + "]";
}






}
