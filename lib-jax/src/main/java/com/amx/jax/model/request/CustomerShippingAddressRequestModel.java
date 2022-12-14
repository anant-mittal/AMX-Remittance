package com.amx.jax.model.request;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import com.amx.jax.model.response.fx.AddressTypeDto;
/** this model represents customer shipping address **/
public class CustomerShippingAddressRequestModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal countryId;

	private BigDecimal stateId;

	private BigDecimal districtId;

	//@NotNull(message="Mobile may not be null")
	//@Pattern(regexp = "^[1-9]\\d*$",message="Invalid Mobile No")
	//@Size(min = 8)	
	private String mobile;

	/** country telephone prefix */
	//@NotNull(message="telPrefix may not be null")
	//@Pattern(regexp = "^[1-9]\\d*$",message="Invalid Tele Prefix")
	private String telPrefix;
	
	String block;
	@Pattern(regexp = "^[\\u0621-\\u064A\\u0660-\\u0669A-Za-z0-9 ,.-]+$", message = "Invalid Street, Only AlphaNumeric .,- are allowed")
	String street;
	String flatNo;
	String buildingNo;
	private BigDecimal cityId;
	private BigDecimal areaCode;
	private String addressType;
	private AddressTypeDto addressTypeDto;
	private BigDecimal govermentId;
	private BigDecimal govermentAreaId;
	
	
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


	public String getModelType() {
		return "fc-sale-shipping-address-save";
	}

	public BigDecimal getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(BigDecimal areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public AddressTypeDto getAddressTypeDto() {
		return addressTypeDto;
	}

	public void setAddressTypeDto(AddressTypeDto addressTypeDto) {
		this.addressTypeDto = addressTypeDto;
	}

	public BigDecimal getGovermentId() {
		return govermentId;
	}

	public void setGovermentId(BigDecimal govermentId) {
		this.govermentId = govermentId;
	}

	public BigDecimal getGovermentAreaId() {
		return govermentAreaId;
	}

	public void setGovermentAreaId(BigDecimal govermentAreaId) {
		this.govermentAreaId = govermentAreaId;
	}

	
}