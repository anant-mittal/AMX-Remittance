package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class CountryWiseCountOrderDto {
	private String countryName;
	private BigDecimal countryId;
	private BigDecimal totalCount;
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}
	
}
