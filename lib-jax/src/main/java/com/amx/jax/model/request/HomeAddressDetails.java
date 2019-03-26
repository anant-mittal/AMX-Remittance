package com.amx.jax.model.request;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class HomeAddressDetails {
	
	@ApiMockModelProperty(example="50")
	private BigDecimal contactTypeId;
	
	@ApiMockModelProperty(example="91")
	private BigDecimal countryId;
	
	@ApiMockModelProperty(example="584")
	private BigDecimal stateId;
	
	@ApiMockModelProperty(example="4165")
	private BigDecimal districtId;
	
	@ApiMockModelProperty(example="12760")
	private BigDecimal cityId;
	
	@ApiMockModelProperty(example="Runwall")
	private String house;
	
	@ApiMockModelProperty(example="5")
	private String flat;
	
	@ApiMockModelProperty(example="Gandhi Road")
	private String street;
	
	@ApiMockModelProperty(example="1011")
	private String block;

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

	public BigDecimal getContactTypeId() {
		return contactTypeId;
	}

	public void setContactTypeId(BigDecimal contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	@Override
	public String toString() {
		return "HomeAddressDetails [contactTypeId=" + contactTypeId + ", countryId=" + countryId + ", stateId="
				+ stateId + ", districtId=" + districtId + ", cityId=" + cityId + ", house=" + house + ", flat=" + flat
				+ ", street=" + street + ", block=" + block + "]";
	}

}
