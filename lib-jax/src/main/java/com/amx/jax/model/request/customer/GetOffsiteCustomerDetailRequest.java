package com.amx.jax.model.request.customer;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class GetOffsiteCustomerDetailRequest {

	@NotNull
	String identityInt;
	@NotNull
	BigDecimal identityType;
	Date issueDate;
	Date expiryDate;
	Date dateOfBirth;
	
	

	public GetOffsiteCustomerDetailRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GetOffsiteCustomerDetailRequest(String identityInt, BigDecimal identityType) {
		super();
		this.identityInt = identityInt;
		this.identityType = identityType;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public BigDecimal getIdentityType() {
		return identityType;
	}

	public void setIdentityType(BigDecimal identityType) {
		this.identityType = identityType;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
