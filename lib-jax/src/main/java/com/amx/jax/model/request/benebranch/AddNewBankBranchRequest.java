package com.amx.jax.model.request.benebranch;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class AddNewBankBranchRequest {

	@NotNull
	@ApiMockModelProperty(example = "94")
	BigDecimal countryId;
	
	@NotNull
	@ApiMockModelProperty(example = "4")
	BigDecimal currencyId;
	
	@NotNull
	@ApiMockModelProperty(example = "2258")
	BigDecimal bankId;
	
	@NotNull
	@ApiMockModelProperty(example="testbranch")
	String branchName;
	
	@NotNull
	@ApiMockModelProperty(example="testbranch addr")
	String branchAddress;
	
	@ApiMockModelProperty(example="testifsc")
	String ifscCode;
	
	String swift;
	
	@ApiMockModelProperty(example = "585")
	@NotNull
	BigDecimal stateId;
	
	@NotNull
	@ApiMockModelProperty(example = "4166")
	BigDecimal districtId;
	
	@NotNull
	@ApiMockModelProperty(example = "31530")
	BigDecimal cityId;
	

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
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

}
