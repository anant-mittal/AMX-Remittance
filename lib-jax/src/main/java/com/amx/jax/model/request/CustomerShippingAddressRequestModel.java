package com.amx.jax.model.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/** this model represents customer shipping address **/
public class CustomerShippingAddressRequestModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message="Country Id may not be null")
	private BigDecimal countryId;

	@NotNull(message="State Id may not be null")
	private BigDecimal stateId;

	@NotNull(message="District Id may not be null")
	private BigDecimal districtId;

	@NotNull(message="Mobile may not be null")
	@Pattern(regexp = "^[1-8]\\d*$",message="Invalid Mobile No")
	@Size(min = 8)	
	private String mobile;

	/** country telephone prefix */
	@NotNull(message="telPrefix may not be null")
	@Pattern(regexp = "^[1-9]\\d*$",message="Invalid Tele Prefix")
	private String telPrefix;
	
	String block;
	String street;
	String flatNo;
	String buildingNo;
	private BigDecimal cityId;
	private String area;
	private String modelType;
	

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

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public BigDecimal getCityId() {
		return cityId;
	}

	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getModelType() {
		return "fc-sale-shipping-address-save";
	}

	
}