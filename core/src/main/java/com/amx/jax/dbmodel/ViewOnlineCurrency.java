package com.amx.jax.dbmodel;
/**
 * Author :rabil
 */

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_VW_CURRENCY_ONLINE")
public class ViewOnlineCurrency {

	private BigDecimal currencyId;
	private String currencyCode;
	private String currencyName;
	private String quoteName;
	private BigDecimal countryId;
	private BigDecimal decinalNumber;

	private BigDecimal fundMinRate;
	private BigDecimal fundMaxRate;
	private String isActive;

	@Id
	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "CURRENCY_CODE")
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Column(name = "CURRENCY_NAME")
	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Column(name = "QUOTE_NAME")
	public String getQuoteName() {
		return quoteName;
	}

	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name = "DECIMAL_NUMBER")
	public BigDecimal getDecinalNumber() {
		return decinalNumber;
	}

	public void setDecinalNumber(BigDecimal decimalNumber) {
		this.decinalNumber = decimalNumber;
	}

	@Column(name = "FUND_MIN_RATE")
	public BigDecimal getFundMinRate() {
		return fundMinRate;
	}

	public void setFundMinRate(BigDecimal fundMinRate) {
		this.fundMinRate = fundMinRate;
	}

	@Column(name = "FUND_MAX_RATE")
	public BigDecimal getFundMaxRate() {
		return fundMaxRate;
	}

	public void setFundMaxRate(BigDecimal fundMaxRate) {
		this.fundMaxRate = fundMaxRate;
	}
	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
}
