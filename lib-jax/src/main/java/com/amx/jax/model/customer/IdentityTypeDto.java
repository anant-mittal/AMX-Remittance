package com.amx.jax.model.customer;

import java.math.BigDecimal;

public class IdentityTypeDto {

	BigDecimal identityTypeId;
	String identityType;

	
	
	public IdentityTypeDto() {
		super();
	}

	public IdentityTypeDto(BigDecimal identityTypeId, String identityType) {
		super();
		this.identityTypeId = identityTypeId;
		this.identityType = identityType;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

}
