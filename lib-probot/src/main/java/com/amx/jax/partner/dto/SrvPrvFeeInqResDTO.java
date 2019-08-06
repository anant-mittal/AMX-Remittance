package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

public class SrvPrvFeeInqResDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2594412775985894857L;
	
	private BigDecimal exchangeRateWithPips;
	private BigDecimal exchangeRateBase;
	private BigDecimal exchangeRateWithLocalAndSettlementCurrency;
	private BigDecimal exchangeRateByServiceProvider;
	private BigDecimal commissionAmount;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private BigDecimal grossAmount;
	private BigDecimal margin;
	private HomeSendSrvcProviderInfo homeSendSrvcProviderInfo;
	private Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails;
	
	public BigDecimal getExchangeRateWithPips() {
		return exchangeRateWithPips;
	}
	public void setExchangeRateWithPips(BigDecimal exchangeRateWithPips) {
		this.exchangeRateWithPips = exchangeRateWithPips;
	}
	
	public BigDecimal getExchangeRateWithLocalAndSettlementCurrency() {
		return exchangeRateWithLocalAndSettlementCurrency;
	}
	public void setExchangeRateWithLocalAndSettlementCurrency(BigDecimal exchangeRateWithLocalAndSettlementCurrency) {
		this.exchangeRateWithLocalAndSettlementCurrency = exchangeRateWithLocalAndSettlementCurrency;
	}
	
	public BigDecimal getExchangeRateByServiceProvider() {
		return exchangeRateByServiceProvider;
	}
	public void setExchangeRateByServiceProvider(BigDecimal exchangeRateByServiceProvider) {
		this.exchangeRateByServiceProvider = exchangeRateByServiceProvider;
	}
	
	public BigDecimal getCommissionAmount() {
		return commissionAmount;
	}
	public void setCommissionAmount(BigDecimal commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	
	public BigDecimal getLocalAmount() {
		return localAmount;
	}
	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}
	
	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}
	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}
	
	public BigDecimal getGrossAmount() {
		return grossAmount;
	}
	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}
	
	public HomeSendSrvcProviderInfo getHomeSendInfoDTO() {
		return homeSendSrvcProviderInfo;
	}
	public void setHomeSendInfoDTO(HomeSendSrvcProviderInfo homeSendSrvcProviderInfo) {
		this.homeSendSrvcProviderInfo = homeSendSrvcProviderInfo;
	}
	
	public BigDecimal getExchangeRateBase() {
		return exchangeRateBase;
	}
	public void setExchangeRateBase(BigDecimal exchangeRateWithoutPips) {
		this.exchangeRateBase = exchangeRateWithoutPips;
	}
	
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
	
	public Map<DISCOUNT_TYPE, ExchangeDiscountInfo> getCustomerDiscountDetails() {
		return customerDiscountDetails;
	}
	public void setCustomerDiscountDetails(Map<DISCOUNT_TYPE, ExchangeDiscountInfo> discountPipsDetails) {
		this.customerDiscountDetails = discountPipsDetails;
	}
		
}
