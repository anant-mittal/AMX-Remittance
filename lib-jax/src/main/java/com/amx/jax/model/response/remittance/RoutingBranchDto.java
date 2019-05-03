package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class RoutingBranchDto {
	private BigDecimal bankBranchId;
	private BigDecimal branchCode;
	private String branchFullName;
	
	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
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
	
}
