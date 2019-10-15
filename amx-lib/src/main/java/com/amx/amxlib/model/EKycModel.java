package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.Date;

public class EKycModel {
	private BigDecimal identityTypeId;
	private String isActive;
	private Date lastUpdatedDate;
	
	
	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}
	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
}
