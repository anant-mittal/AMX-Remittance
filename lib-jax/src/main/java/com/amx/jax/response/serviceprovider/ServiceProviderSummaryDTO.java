package com.amx.jax.response.serviceprovider;

import java.math.BigDecimal;

public class ServiceProviderSummaryDTO {
	private String sendPayIndicator;
	private BigDecimal totalCount;
	private BigDecimal unmatchedCount;
	private BigDecimal commission;
	private BigDecimal exchangeGain;
	private BigDecimal totalTransactionCount;
	private BigDecimal totalUnmatchedCount;
	private BigDecimal totalExchangeGain;
	private BigDecimal totalCommission;
	public BigDecimal getTotalTransactionCount() {
		return totalTransactionCount;
	}
	public void setTotalTransactionCount(BigDecimal totalTransactionCount) {
		this.totalTransactionCount = totalTransactionCount;
	}
	public BigDecimal getTotalUnmatchedCount() {
		return totalUnmatchedCount;
	}
	public void setTotalUnmatchedCount(BigDecimal totalUnmatchedCount) {
		this.totalUnmatchedCount = totalUnmatchedCount;
	}
	public BigDecimal getTotalExchangeGain() {
		return totalExchangeGain;
	}
	public void setTotalExchangeGain(BigDecimal totalExchangeGain) {
		this.totalExchangeGain = totalExchangeGain;
	}
	public BigDecimal getTotalCommission() {
		return totalCommission;
	}
	public void setTotalCommission(BigDecimal totalCommission) {
		this.totalCommission = totalCommission;
	}
	public String getSendPayIndicator() {
		return sendPayIndicator;
	}
	public void setSendPayIndicator(String sendPayIndicator) {
		this.sendPayIndicator = sendPayIndicator;
	}
	public BigDecimal getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}
	public BigDecimal getUnmatchedCount() {
		return unmatchedCount;
	}
	public void setUnmatchedCount(BigDecimal unmatchedCount) {
		this.unmatchedCount = unmatchedCount;
	}
	public BigDecimal getCommission() {
		return commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	public BigDecimal getExchangeGain() {
		return exchangeGain;
	}
	public void setExchangeGain(BigDecimal exchangeGain) {
		this.exchangeGain = exchangeGain;
	}
}
