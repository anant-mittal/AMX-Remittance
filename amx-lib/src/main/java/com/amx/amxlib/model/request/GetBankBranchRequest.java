package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class GetBankBranchRequest {

	BigDecimal bankId;

	BigDecimal countryId;

	String ifscCode;

	String swift;

	String branchName;
	
	

	public GetBankBranchRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GetBankBranchRequest(BigDecimal bankId, BigDecimal countryId, String ifscCode, String swift,
			String branchName) {
		super();
		this.bankId = bankId;
		this.countryId = countryId;
		this.ifscCode = ifscCode;
		this.swift = swift;
		this.branchName = branchName;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
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

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Override
	public String toString() {
		return "GetBankBranchRequest [bankId=" + bankId + ", countryId=" + countryId + ", ifscCode=" + ifscCode
				+ ", swift=" + swift + ", branchName=" + branchName + "]";
	}

}
