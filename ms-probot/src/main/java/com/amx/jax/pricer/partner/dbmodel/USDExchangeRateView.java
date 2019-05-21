package com.amx.jax.pricer.partner.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JAX_VW_CASH_USD_RATE")
public class USDExchangeRateView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal idNo;
	private Date currencyDate;
	private String localCurrencyCode;
	private BigDecimal usdLocalRate;
	private Date createdDate;
	private Date updatedDate;
	private String modifiedBy;
	private String createdBy;
	private String bankCode;
	private String destinationCurrencyCode;
	
	@Id
	@Column(name="IDNO")
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	
	@Column(name="CURRENCY_DATE")
	public Date getCurrencyDate() {
		return currencyDate;
	}
	public void setCurrencyDate(Date currencyDate) {
		this.currencyDate = currencyDate;
	}
	
	@Column(name="LOCAL_CURRENCY_CODE")
	public String getLocalCurrencyCode() {
		return localCurrencyCode;
	}
	public void setLocalCurrencyCode(String localCurrencyCode) {
		this.localCurrencyCode = localCurrencyCode;
	}
	
	@Column(name="USD_LOCAL_RATE")
	public BigDecimal getUsdLocalRate() {
		return usdLocalRate;
	}
	public void setUsdLocalRate(BigDecimal usdLocalRate) {
		this.usdLocalRate = usdLocalRate;
	}
	
	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="UPDATED_DATE")
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name="BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	@Column(name="DESTINATION_CURRENCY_CODE")
	public String getDestinationCurrencyCode() {
		return destinationCurrencyCode;
	}
	public void setDestinationCurrencyCode(String destinationCurrencyCode) {
		this.destinationCurrencyCode = destinationCurrencyCode;
	}

}
