package com.amx.jax.pricer.partner.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JAX_VW_HOME_SRV_RATE")
public class ServiceProviderRateView  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal idNo;
	private BigDecimal bankId;
	private BigDecimal countryId;
	private BigDecimal currencyId;
	private BigDecimal remittanceId;
	private BigDecimal deliveryId;
	private BigDecimal margin;
	private BigDecimal localSettlementCurrencyRate;
	private BigDecimal localSettlementCurrencySellRate;
	private BigDecimal foreignSettlementCurrencyRate;
	private BigDecimal localforeignCurrencySellRate;
	private BigDecimal foreignSettlementCurrencyCorpRate;
	private BigDecimal marginCorporate;
	private BigDecimal localforeignCurrencySellRateCorp;
	
	private String bankCode;
	private String countryCode;
	private String currencyCode;
	private String remittanceCode;
	private String deliveryCode;
	
	@Id
	@Column(name="IDNO")
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	
	@Column(name="BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	
	@Column(name="CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	
	@Column(name="REMITTANCE_ID")
	public BigDecimal getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(BigDecimal remittanceId) {
		this.remittanceId = remittanceId;
	}
	
	@Column(name="DELIVERY_ID")
	public BigDecimal getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(BigDecimal deliveryId) {
		this.deliveryId = deliveryId;
	}
	
	@Column(name="MARGIN")
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
	
	@Column(name="KD_USD_RATE")
	public BigDecimal getLocalSettlementCurrencyRate() {
		return localSettlementCurrencyRate;
	}
	public void setLocalSettlementCurrencyRate(BigDecimal localSettlementCurrencyRate) {
		this.localSettlementCurrencyRate = localSettlementCurrencyRate;
	}
	
	@Column(name="USD_KWD_SELL_RATE")
	public BigDecimal getLocalSettlementCurrencySellRate() {
		return localSettlementCurrencySellRate;
	}
	public void setLocalSettlementCurrencySellRate(BigDecimal localSettlementCurrencySellRate) {
		this.localSettlementCurrencySellRate = localSettlementCurrencySellRate;
	}
	
	@Column(name="FC_USD_RATE")
	public BigDecimal getForeignSettlementCurrencyRate() {
		return foreignSettlementCurrencyRate;
	}
	public void setForeignSettlementCurrencyRate(BigDecimal foreignSettlementCurrencyRate) {
		this.foreignSettlementCurrencyRate = foreignSettlementCurrencyRate;
	}
	
	@Column(name="KWD_FC_SELL_RATE")
	public BigDecimal getLocalforeignCurrencySellRate() {
		return localforeignCurrencySellRate;
	}
	public void setLocalforeignCurrencySellRate(BigDecimal localforeignCurrencySellRate) {
		this.localforeignCurrencySellRate = localforeignCurrencySellRate;
	}
	
	@Column(name="FC_USD_RATE_CORP")
	public BigDecimal getForeignSettlementCurrencyCorpRate() {
		return foreignSettlementCurrencyCorpRate;
	}
	public void setForeignSettlementCurrencyCorpRate(BigDecimal foreignSettlementCurrencyCorpRate) {
		this.foreignSettlementCurrencyCorpRate = foreignSettlementCurrencyCorpRate;
	}
	
	@Column(name="MARGIN_CORPORATE")
	public BigDecimal getMarginCorporate() {
		return marginCorporate;
	}
	public void setMarginCorporate(BigDecimal marginCorporate) {
		this.marginCorporate = marginCorporate;
	}
	
	@Column(name="KWD_FC_SELL_RATE_CORPORATE")
	public BigDecimal getLocalforeignCurrencySellRateCorp() {
		return localforeignCurrencySellRateCorp;
	}
	public void setLocalforeignCurrencySellRateCorp(BigDecimal localforeignCurrencySellRateCorp) {
		this.localforeignCurrencySellRateCorp = localforeignCurrencySellRateCorp;
	}
	
	@Column(name="BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	@Column(name="COUNTRY_CODE")
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	@Column(name="CURRENCY_CODE")
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Column(name="REMITTANCE_CODE")
	public String getRemittanceCode() {
		return remittanceCode;
	}
	public void setRemittanceCode(String remittanceCode) {
		this.remittanceCode = remittanceCode;
	}
	
	@Column(name="DELIVERY_CODE")
	public String getDeliveryCode() {
		return deliveryCode;
	}
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

}
