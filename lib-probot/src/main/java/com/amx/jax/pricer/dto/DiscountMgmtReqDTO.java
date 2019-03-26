package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

public class DiscountMgmtReqDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2594412775985894857L;

	@NotNull(message = "Discount Type Can not be Null or Empty")
	private List<DISCOUNT_TYPE> discountType;

	private BigDecimal countryId;

	private BigDecimal currencyId;

	public List<DISCOUNT_TYPE> getDiscountType() {
		return discountType;
	}

	public void setDiscountType(List<DISCOUNT_TYPE> discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

}
