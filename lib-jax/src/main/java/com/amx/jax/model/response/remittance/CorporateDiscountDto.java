package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class CorporateDiscountDto {

	BigDecimal corpDiscount = BigDecimal.ZERO;
	BigDecimal corpDiscountId;
	public BigDecimal getCorpDiscount() {
		return corpDiscount;
	}
	public void setCorpDiscount(BigDecimal corpDiscount) {
		this.corpDiscount = corpDiscount;
	}
	public BigDecimal getCorpDiscountId() {
		return corpDiscountId;
	}
	public void setCorpDiscountId(BigDecimal corpDiscountId) {
		this.corpDiscountId = corpDiscountId;
	}
	
}
