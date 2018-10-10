package com.amx.jax.model.request;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class LocalAddressDetails {

	@ApiModelProperty(example="49")
	private BigDecimal contactTypeId;
	
	@ApiModelProperty(example="1011")
	private String block;
	
	@ApiModelProperty(example="Gandhi Road")
	private String street;
	
	@ApiModelProperty(example="Runwall")
	private String house;
	
	@ApiModelProperty(example="5")
	private String flat;
	
	@ApiModelProperty(example="91")
	private BigDecimal countryId;
	
	@ApiModelProperty(example="584")
	private BigDecimal stateId;
	
	@ApiModelProperty(example="4165")
	private BigDecimal districtId;
	
	@ApiModelProperty(example="12760")
	private BigDecimal cityId;

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

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlat(String flat) {
		this.flat = flat;
	}

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

	public BigDecimal getContactTypeId() {
		return contactTypeId;
	}

	public void setContactTypeId(BigDecimal contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

}
