package com.amx.amxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

/** THis model represents customer's home address */
public class CustomerHomeAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private BigDecimal countryId;

	@NotNull
	private BigDecimal stateId;

	@NotNull
	private BigDecimal districtId;

	@NotNull
	@Pattern(regexp = "^[1-9]\\d*$",message="Invalid Mobile No")
	@Size(min = 1)	
	private String mobile;

	/** country telephone prefix */
	@NotNull
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="Invalid Tele Prefix")
	private String telPrefix;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "CustomerHomeAddress [countryId=" + countryId + ", stateId=" + stateId + ", districtId=" + districtId
				+ ", mobile=" + mobile + ", telPrefix=" + telPrefix + "]";
	}

	public String getTelPrefix() {
		return telPrefix;
	}

	public void setTelPrefix(String telPrefix) {
		this.telPrefix = telPrefix;
	}

}
