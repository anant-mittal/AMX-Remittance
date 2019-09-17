package com.amx.jax.dbmodel.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_FC_STOCK")
public class UserFcStockView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private BigDecimal stockId;
	
	@Column(name = "LOCCOD")
	private BigDecimal locationCode;
	
	@Column(name = "COUNTRY_BRANCH_ID")
	private BigDecimal countryBranchId;
	
	@Column(name = "ORAUSER")
	private String oracleUser;
	
	@Column(name = "LOGDAT")
	private Date logDate;
	
	@Column(name = "CURCOD")
	private String currencyCode;
	
	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;
	
	@Column(name = "DENOID")
	private BigDecimal denominationCode;
	
	@Column(name = "DENOMINATION_ID")
	private BigDecimal denominationId;
	
	@Column(name = "CURRENT_STOCK")
	private BigDecimal currentStock;

	
	public BigDecimal getStockId() {
		return stockId;
	}
	public void setStockId(BigDecimal stockId) {
		this.stockId = stockId;
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
	
	public BigDecimal getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(BigDecimal locationCode) {
		this.locationCode = locationCode;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public BigDecimal getDenominationCode() {
		return denominationCode;
	}
	public void setDenominationCode(BigDecimal denominationCode) {
		this.denominationCode = denominationCode;
	}
}
