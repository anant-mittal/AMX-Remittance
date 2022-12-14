package com.amx.jax.dbmodel.remittance;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_EX_LOYALTY_ENCASH")
public class VwLoyalityEncash {

	@Column(name = "SEQ_ID")
	@Id
	BigDecimal seqId;

	@Column(name = "LOYALTY_POINT")
	BigDecimal loyalityPoint;

	@Column(name = "EQUIVALENT_AMOUNT")
	BigDecimal equivalentAmount;

	public BigDecimal getSeqId() {
		return seqId;
	}

	public void setSeqId(BigDecimal seqId) {
		this.seqId = seqId;
	}

	public BigDecimal getEquivalentAmount() {
		return equivalentAmount;
	}

	public void setEquivalentAmount(BigDecimal equivalentAmount) {
		this.equivalentAmount = equivalentAmount;
	}

	public BigDecimal getLoyalityPoint() {
		return loyalityPoint;
	}

	public void setLoyalityPoint(BigDecimal loyalityPoint) {
		this.loyalityPoint = loyalityPoint;
	}

}
