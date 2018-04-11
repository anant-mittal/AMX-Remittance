package com.amx.amxlib.model;

import com.amx.amxlib.model.request.GetBankBranchRequest;

public class BranchSearchNotificationModel {

	String customerName;

	String identityId;
	
	String bankFullName;

	GetBankBranchRequest customerQuery;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public GetBankBranchRequest getCustomerQuery() {
		return customerQuery;
	}

	public void setCustomerQuery(GetBankBranchRequest customerQuery) {
		this.customerQuery = customerQuery;
	}

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	
}
