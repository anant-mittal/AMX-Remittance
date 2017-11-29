package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class BeneCountryDTO {

	
	
	private BigDecimal idNo;

	private BigDecimal customerId;

	private BigDecimal beneCountry;

	private BigDecimal applicationCountry;

	private String countryCode;

	private String countryName;
	

	private BigDecimal orsStatus;
	
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getBeneCountry() {
		return beneCountry;
	}
	public void setBeneCountry(BigDecimal beneCountry) {
		this.beneCountry = beneCountry;
	}
	public BigDecimal getApplicationCountry() {
		return applicationCountry;
	}
	public void setApplicationCountry(BigDecimal applicationCountry) {
		this.applicationCountry = applicationCountry;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public BigDecimal getOrsStatus() {
		return orsStatus;
	}
	public void setOrsStatus(BigDecimal orsStatus) {
		this.orsStatus = orsStatus;
	}
	
}
