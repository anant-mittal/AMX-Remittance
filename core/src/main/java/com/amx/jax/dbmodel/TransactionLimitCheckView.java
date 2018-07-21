package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_EX_CHANNEL_TRNX_LIMIT")
public class TransactionLimitCheckView implements Serializable{

	private static final long serialVersionUID = 1L;
	private String channel;
	private BigDecimal complianceChkLimit;
	private BigDecimal overallChkLimit;
	
	@Id
	@Column(name = "CHANNEL")
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	@Column(name = "COMPLIANCE_CHK_LIMIT")
	public BigDecimal getComplianceChkLimit() {
		return complianceChkLimit;
	}
	
	public void setComplianceChkLimit(BigDecimal complianceChkLimit) {
		this.complianceChkLimit = complianceChkLimit;
	}
	
	@Column(name = "OVERALL_CHK_LIMIT")
	public BigDecimal getOverallChkLimit() {
		return overallChkLimit;
	}
	public void setOverallChkLimit(BigDecimal overallChkLimit) {
		this.overallChkLimit = overallChkLimit;
	}
}
