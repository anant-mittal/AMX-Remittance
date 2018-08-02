package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class CommonRequest {
BigDecimal coutnryId;
BigDecimal stateId;
BigDecimal districtId;
BigDecimal cityId;
public BigDecimal getCoutnryId() {
	return coutnryId;
}
public void setCoutnryId(BigDecimal coutnryId) {
	this.coutnryId = coutnryId;
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
@Override
public String toString() {
	return "CommonRequest [coutnryId=" + coutnryId + ", stateId=" + stateId + ", districtId=" + districtId + ", cityId="
			+ cityId + "]";
}


}
