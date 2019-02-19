package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AmountSlabDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal pipsMasterId;

	private BigDecimal countryId;

	private String countryName;

	private BigDecimal currencyId;

	private String currencyName;

	private BigDecimal bankId;

	private String bankName;

	private BigDecimal fromAmount;

	private BigDecimal toAmount;

	private BigDecimal discountPips;

	public BigDecimal getPipsMasterId() {
		return pipsMasterId;
	}

	public void setPipsMasterId(BigDecimal pipsMasterId) {
		this.pipsMasterId = pipsMasterId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public BigDecimal getDiscountPips() {
		return discountPips;
	}

	public void setDiscountPips(BigDecimal discountPips) {
		this.discountPips = discountPips;
	}

}
