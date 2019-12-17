package com.amx.jax.dbmodel.fx;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EX_V_CUR_STOCK")
public class ForeignCurrencyStockView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "CURRENCY_ID")
	BigDecimal currencyId;
	
	@Column(name = "CURRENCY_CODE")
	String currencyCode;
	
	@Column(name = "CURRENCY_NAME")
	String currencyName;
	
	@Column(name = "BALANCE")
	BigDecimal totalStockBalance;
	
	@Column(name = "BALANCE_FOR_FC_SALE")
	BigDecimal maxStockBalance;
	
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
	public BigDecimal getTotalStockBalance() {
		return totalStockBalance;
	}
	public void setTotalStockBalance(BigDecimal totalStockBalance) {
		this.totalStockBalance = totalStockBalance;
	}
	
	public BigDecimal getMaxStockBalance() {
		return maxStockBalance;
	}
	public void setMaxStockBalance(BigDecimal maxStockBalance) {
		this.maxStockBalance = maxStockBalance;
	}

}
