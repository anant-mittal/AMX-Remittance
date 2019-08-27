package com.amx.jax.model.request.benebranch;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ListBeneBankOrCashRequest {

	@NotNull
	@ApiMockModelProperty(example = "94")
	BigDecimal countryId;

	@NotNull
	@ApiMockModelProperty(example = "4")
	BigDecimal currencyId;

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
