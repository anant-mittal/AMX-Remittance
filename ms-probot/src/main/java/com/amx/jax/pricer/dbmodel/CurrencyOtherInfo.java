package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_CURRENCY_OTHINFO")
public class CurrencyOtherInfo implements Serializable {

	private static final long serialVersionUID = -4417662409625790419L;

	@Id
	@Column(name = "CURRENCY_OTHINFO_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal currencyOthId;

	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;

	@Column(name = "FUND_MIN_RATE")
	private BigDecimal fundMinRate;

	@Column(name = "FUND_MAX_RATE")
	private BigDecimal fundMaxRate;

	@Column(name = "CASH_MIN_RATE")
	private BigDecimal cashMinRate;

	@Column(name = "CASH_MAX_RATE")
	private BigDecimal cashMaxRate;

	@Column(name = "ONLINE_IND")
	private String onlineInd;

	@Column(name = "CBK_PRINT_IND")
	private String cbkPrintInd;

	@Column(name = "CBK_SORT_IND")
	private BigDecimal cbkSortInd;

	@Column(name = "IS_ACTIVE")
	private String isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "HIGH_VALUE")
	private BigDecimal highValue;

	@Column(name = "AVERAGE_RATE")
	private BigDecimal averageRate;

	@Column(name = "PLACE_ORDER_LIMIT")
	private BigDecimal placeOrderLimit;

	public BigDecimal getCurrencyOthId() {
		return currencyOthId;
	}

	public void setCurrencyOthId(BigDecimal currencyOthId) {
		this.currencyOthId = currencyOthId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getFundMinRate() {
		return fundMinRate;
	}

	public void setFundMinRate(BigDecimal fundMinRate) {
		this.fundMinRate = fundMinRate;
	}

	public BigDecimal getFundMaxRate() {
		return fundMaxRate;
	}

	public void setFundMaxRate(BigDecimal fundMaxRate) {
		this.fundMaxRate = fundMaxRate;
	}

	public BigDecimal getCashMinRate() {
		return cashMinRate;
	}

	public void setCashMinRate(BigDecimal cashMinRate) {
		this.cashMinRate = cashMinRate;
	}

	public BigDecimal getCashMaxRate() {
		return cashMaxRate;
	}

	public void setCashMaxRate(BigDecimal cashMaxRate) {
		this.cashMaxRate = cashMaxRate;
	}

	public String getOnlineInd() {
		return onlineInd;
	}

	public void setOnlineInd(String onlineInd) {
		this.onlineInd = onlineInd;
	}

	public String getCbkPrintInd() {
		return cbkPrintInd;
	}

	public void setCbkPrintInd(String cbkPrintInd) {
		this.cbkPrintInd = cbkPrintInd;
	}

	public BigDecimal getCbkSortInd() {
		return cbkSortInd;
	}

	public void setCbkSortInd(BigDecimal cbkSortInd) {
		this.cbkSortInd = cbkSortInd;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public BigDecimal getHighValue() {
		return highValue;
	}

	public void setHighValue(BigDecimal highValue) {
		this.highValue = highValue;
	}

	public BigDecimal getAverageRate() {
		return averageRate;
	}

	public void setAverageRate(BigDecimal averageRate) {
		this.averageRate = averageRate;
	}

	public BigDecimal getPlaceOrderLimit() {
		return placeOrderLimit;
	}

	public void setPlaceOrderLimit(BigDecimal placeOrderLimit) {
		this.placeOrderLimit = placeOrderLimit;
	}

}
