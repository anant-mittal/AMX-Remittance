package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

public class FxDeliveryTimeSlotDto {

	private BigDecimal deliveryTimeSlotId;
	private BigDecimal countryId;
	private BigDecimal companyId;
	private BigDecimal startTime;
	private BigDecimal endTime;
	private BigDecimal officeStartTime;
	private BigDecimal officeEndTime;
	private BigDecimal timeInterval;
	private String isActive;
	private String remarks;
	public BigDecimal getDeliveryTimeSlotId() {
		return deliveryTimeSlotId;
	}
	public void setDeliveryTimeSlotId(BigDecimal deliveryTimeSlotId) {
		this.deliveryTimeSlotId = deliveryTimeSlotId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	public BigDecimal getStartTime() {
		return startTime;
	}
	public void setStartTime(BigDecimal startTime) {
		this.startTime = startTime;
	}
	public BigDecimal getEndTime() {
		return endTime;
	}
	public void setEndTime(BigDecimal endTime) {
		this.endTime = endTime;
	}
	public BigDecimal getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(BigDecimal timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BigDecimal getOfficeStartTime() {
		return officeStartTime;
	}
	public void setOfficeStartTime(BigDecimal officeStartTime) {
		this.officeStartTime = officeStartTime;
	}
	public BigDecimal getOfficeEndTime() {
		return officeEndTime;
	}
	public void setOfficeEndTime(BigDecimal officeEndTime) {
		this.officeEndTime = officeEndTime;
	}
	
	
}
