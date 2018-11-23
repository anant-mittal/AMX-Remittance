package com.amx.jax.model.response.fx;

import java.io.Serializable;
import java.math.BigDecimal;

public class FxExchangeRateDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal cashRateId;
	private BigDecimal applicationCountryId;
	private BigDecimal baseCurrencyId;

	private BigDecimal fxCurrencyId;

	private String fxCurrencyCode;

	private String fxCurrencyName;

	private String sellIsoCurrencyCode;

	private String purIsoCurrencyCode;

	private BigDecimal salMaxRate;

	private BigDecimal salMinRate;

	private BigDecimal countryBranchId;

	private BigDecimal countryBranchCode;

	private String branchName;

	private String iaActive;

	private String allowFcSale;

	private String allowFcPurchase;

	public BigDecimal getCashRateId() {
		return cashRateId;
	}

	public void setCashRateId(BigDecimal cashRateId) {
		this.cashRateId = cashRateId;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(BigDecimal baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	public BigDecimal getFxCurrencyId() {
		return fxCurrencyId;
	}

	public void setFxCurrencyId(BigDecimal fxCurrencyId) {
		this.fxCurrencyId = fxCurrencyId;
	}

	public String getFxCurrencyCode() {
		return fxCurrencyCode;
	}

	public void setFxCurrencyCode(String fxCurrencyCode) {
		this.fxCurrencyCode = fxCurrencyCode;
	}

	public String getFxCurrencyName() {
		return fxCurrencyName;
	}

	public void setFxCurrencyName(String fxCurrencyName) {
		this.fxCurrencyName = fxCurrencyName;
	}

	public String getSellIsoCurrencyCode() {
		return sellIsoCurrencyCode;
	}

	public void setSellIsoCurrencyCode(String sellIsoCurrencyCode) {
		this.sellIsoCurrencyCode = sellIsoCurrencyCode;
	}

	public String getPurIsoCurrencyCode() {
		return purIsoCurrencyCode;
	}

	public void setPurIsoCurrencyCode(String purIsoCurrencyCode) {
		this.purIsoCurrencyCode = purIsoCurrencyCode;
	}

	public BigDecimal getSalMaxRate() {
		return salMaxRate;
	}

	public void setSalMaxRate(BigDecimal salMaxRate) {
		this.salMaxRate = salMaxRate;
	}

	public BigDecimal getSalMinRate() {
		return salMinRate;
	}

	public void setSalMinRate(BigDecimal salMinRate) {
		this.salMinRate = salMinRate;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public BigDecimal getCountryBranchCode() {
		return countryBranchCode;
	}

	public void setCountryBranchCode(BigDecimal countryBranchCode) {
		this.countryBranchCode = countryBranchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getIaActive() {
		return iaActive;
	}

	public void setIaActive(String iaActive) {
		this.iaActive = iaActive;
	}

	public String getAllowFcSale() {
		return allowFcSale;
	}

	public void setAllowFcSale(String allowFcSale) {
		this.allowFcSale = allowFcSale;
	}

	public String getAllowFcPurchase() {
		return allowFcPurchase;
	}

	public void setAllowFcPurchase(String allowFcPurchase) {
		this.allowFcPurchase = allowFcPurchase;
	}
}
