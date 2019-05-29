package com.amx.jax.model.customer;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class UploadCustomerKycRequest {

	@NotNull(message = "IdentityInt may not be null")
	@ApiMockModelProperty(example = "268020406638")
	String identityInt;

	@ApiMockModelProperty(example = "198")
	@NotNull(message = "identityTypeId may not be null")
	BigDecimal identityTypeId;

	@NotNull
	Date expiryDate;

	@NotNull
	String document;

	String remark;

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "UploadCustomerKycRequest [identityInt=" + identityInt + ", identityTypeId=" + identityTypeId
				+ ", expiryDate=" + expiryDate + "]";
	}

}
