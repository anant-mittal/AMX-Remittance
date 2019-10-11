package com.amx.amxlib.model.request;

import java.math.BigDecimal;

import com.amx.jax.model.request.benebranch.BankBranchListRequest;

/**
 * 
 * Use {@link BankBranchListRequest}
 * 
 *
 */
@Deprecated
public class GetBankBranchRequest extends BankBranchListRequest {

	public GetBankBranchRequest() {
		super();
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
}
