package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="JAX_VW_TPC_REVENUE_SUMMARY")
public class ServiceProviderSummaryModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2338859603525488152L;
	@Column(name="UNMATCHED_COMM_SHARE")
	private BigDecimal unmatchedCommission;
	@Column(name="UNMATCHED_EXCHANGE_GAIN")
	private BigDecimal unmatchedExchangeGain;
	@Column(name="UNMATCHED_COUNT")
	private BigDecimal unmatchedCount;
	@Column(name="TOTAL_COUNT")
	private BigDecimal totalTransaction;
	@Column(name="SEND_PAY_INDIC")
	private String sendPayIndicator;
	@Id
	@Column(name="SUMMARY_ID")
	private String summaryId;
	
	public String getSummaryId() {
		return summaryId;
	}
	public void setSummaryId(String summaryId) {
		this.summaryId = summaryId;
	}
	public BigDecimal getUnmatchedCommission() {
		return unmatchedCommission;
	}
	public void setUnmatchedCommission(BigDecimal unmatchedCommission) {
		this.unmatchedCommission = unmatchedCommission;
	}
	public BigDecimal getUnmatchedExchangeGain() {
		return unmatchedExchangeGain;
	}
	public void setUnmatchedExchangeGain(BigDecimal unmatchedExchangeGain) {
		this.unmatchedExchangeGain = unmatchedExchangeGain;
	}
	public BigDecimal getUnmatchedCount() {
		return unmatchedCount;
	}
	public void setUnmatchedCount(BigDecimal unmatchedCount) {
		this.unmatchedCount = unmatchedCount;
	}
	public BigDecimal getTotalTransaction() {
		return totalTransaction;
	}
	public void setTotalTransaction(BigDecimal totalTransaction) {
		this.totalTransaction = totalTransaction;
	}
	public String getSendPayIndicator() {
		return sendPayIndicator;
	}
	public void setSendPayIndicator(String sendPayIndicator) {
		this.sendPayIndicator = sendPayIndicator;
	}
	
	
	
}
