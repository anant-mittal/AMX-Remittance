package com.amx.jax.dbmodel.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_EX_STOCK")
public class UserStockView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STOCK_ID")
	private BigDecimal stockId;
	
	@Column(name = "COUNTRY_ID")
	private BigDecimal countryId;
	
	@Column(name = "COUNTRY_BRANCH_ID")
	private BigDecimal countryBranchId;
	
	@Column(name = "ORACLE_USER")
	private String oracleUser;
	
	@Column(name = "LOG_DATE")
	private Date logDate;
	
	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;
	
	@Column(name = "DENOMINATION_ID")
	private BigDecimal denominationId;
	
	@Column(name = "CURRENT_STOCK")
	private BigDecimal currentStock;
	
	@Column(name = "DENOMINATION_AMOUNT")
	private BigDecimal denominationAmount;
	
	@Column(name = "DENOMINATION_DESC")
	private String denominationDesc;

	
	public BigDecimal getStockId() {
		return stockId;
	}
	public void setStockId(BigDecimal stockId) {
		this.stockId = stockId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}
	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public String getOracleUser() {
		return oracleUser;
	}
	public void setOracleUser(String oracleUser) {
		this.oracleUser = oracleUser;
	}

	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getDenominationId() {
		return denominationId;
	}
	public void setDenominationId(BigDecimal denominationId) {
		this.denominationId = denominationId;
	}

	public BigDecimal getCurrentStock() {
		return currentStock;
	}
	public void setCurrentStock(BigDecimal currentStock) {
		this.currentStock = currentStock;
	}

	public BigDecimal getDenominationAmount() {
		return denominationAmount;
	}
	public void setDenominationAmount(BigDecimal denominationAmount) {
		this.denominationAmount = denominationAmount;
	}
	
	public String getDenominationDesc() {
		return denominationDesc;
	}
	public void setDenominationDesc(String denominationDesc) {
		this.denominationDesc = denominationDesc;
	}

}
