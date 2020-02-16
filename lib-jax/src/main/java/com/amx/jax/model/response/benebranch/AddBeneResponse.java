package com.amx.jax.model.response.benebranch;

import java.math.BigDecimal;

public class AddBeneResponse {

	BigDecimal idNo;
	
	

	public AddBeneResponse() {
		super();
	}

	public AddBeneResponse(BigDecimal idNo) {
		super();
		this.idNo = idNo;
	}

	public BigDecimal getIdNo() {
		return idNo;
	}

	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
}
