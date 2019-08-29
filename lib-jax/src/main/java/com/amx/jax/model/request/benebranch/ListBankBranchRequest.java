package com.amx.jax.model.request.benebranch;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

/**
 * This request is used in branch bene module
 * 
 * @author prashant
 *
 */
public class ListBankBranchRequest {

	@NotNull
	@ApiMockModelProperty(example = "1527")
	BigDecimal bankId;

	@NotNull
	@ApiMockModelProperty(example = "94")
	BigDecimal countryId;

	@ApiMockModelProperty(example = "IDIB000A001")
	String ifscCode;

	String swift;
	
	String branchName;

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

}
