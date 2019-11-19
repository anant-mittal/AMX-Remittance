package com.amx.jax.model.response.benebranch;

import java.math.BigDecimal;

public class BankBranchDto {

	private BigDecimal bankBranchId;
	private String address1;
	private String address2;
	private BigDecimal bankId;
	private BigDecimal branchCode;
	private String branchFullName;
	private BigDecimal cityId;
	private BigDecimal stateId;
	private BigDecimal districtId;
	private String cityName;
	private String stateName;
	private String districtName;
	private String swift;
	private String zipcode;
	private String ifscCode;

	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(BigDecimal branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchFullName() {
		return branchFullName;
	}

	public void setBranchFullName(String branchFullName) {
		this.branchFullName = branchFullName;
	}

	public BigDecimal getCityId() {
		return cityId;
	}

	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	@Override
	public String toString() {
		return "BankBranchView [bankBranchId=" + bankBranchId + ", address1=" + address1 + ", address2=" + address2
				+ ", bankId=" + bankId + ", branchCode=" + branchCode + ", branchFullName=" + branchFullName
				+ ", cityId=" + cityId + ", stateId=" + stateId + ", districtId=" + districtId + ", cityName="
				+ cityName + ", stateName=" + stateName + ", districtName=" + districtName + ", swift=" + swift
				+ ", zipcode=" + zipcode + ", ifscCode=" + ifscCode + "]";
	}

}
