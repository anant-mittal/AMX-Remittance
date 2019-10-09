package com.amx.jax.model.customer;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class CustomerKycData {

	@NotNull
	Date expiryDate;

	String remark;

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
