package com.amx.jax.dbmodel.fx;

/**
 * Author : Rabil
 * Date   : 04/11/2018
 * Purpose :To get the fc sale exchange rate.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_CASH_RATE")
// This view created from the table EX_EVENT_NOTIFICATION and EX_EVENT_MASTER
public class FxExchangeRateView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6960407133995896977L;

	@Id
	@Column(name = "CASH_RATE_ID")
	private BigDecimal cashRateId;
	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;
	@Column(name = "BASE_CURRENCY_ID")
	private BigDecimal baseCurrencyId;
	@Column(name = "ALTERNATIVE_CURRENCY_ID")
	private BigDecimal fxCurrencyId;
	@Column(name = "CURRENCY_CODE")
	private String fxCurrencyCode;
	@Column(name = "CURRENCY_NAME")
	private String fxCurrencyName;
	@Column(name = "SELL_ISO_CURRENCY")
	private String sellIsoCurrencyCode;

	@Column(name = "PUR_ISO_CURRENCY")
	private String purIsoCurrencyCode;

	@Column(name = "SAL_MAX_RATE")
	private BigDecimal salMaxRate;

	@Column(name = "SAL_MIN_RATE")
	private BigDecimal salMinRate;

	@Column(name = "COUNTRY_BRANCH_ID")
	private BigDecimal countryBranchId;

	@Column(name = "BRANCH_ID")
	private BigDecimal countryBranchCode;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "ISACTIVE")
	private String iaActive;

	@Column(name = "ALLOW_FC_SALE")
	private String allowFcSale;

	@Column(name = "ALLOW_FC_PURCHASE")
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
