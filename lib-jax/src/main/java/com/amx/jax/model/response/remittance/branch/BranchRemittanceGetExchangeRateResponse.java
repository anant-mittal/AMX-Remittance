package com.amx.jax.model.response.remittance.branch;

import java.math.BigDecimal;

import com.amx.jax.model.response.ExchangeRateBreakup;

public class BranchRemittanceGetExchangeRateResponse {

	BigDecimal txnFee;
	BigDecimal totalLoyalityPoints;
	BigDecimal maxLoyalityPointsAvailableForTxn;
	ExchangeRateBreakup exRateBreakup;
	Boolean canRedeemLoyalityPoints;

	public BigDecimal getTxnFee() {
		return txnFee;
	}

	public void setTxnFee(BigDecimal txnFee) {
		this.txnFee = txnFee;
	}

	public BigDecimal getTotalLoyalityPoints() {
		return totalLoyalityPoints;
	}

	public void setTotalLoyalityPoints(BigDecimal totalLoyalityPoints) {
		this.totalLoyalityPoints = totalLoyalityPoints;
	}

	public BigDecimal getMaxLoyalityPointsAvailableForTxn() {
		return maxLoyalityPointsAvailableForTxn;
	}

	public void setMaxLoyalityPointsAvailableForTxn(BigDecimal maxLoyalityPointsAvailableForTxn) {
		this.maxLoyalityPointsAvailableForTxn = maxLoyalityPointsAvailableForTxn;
	}

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

	public Boolean getCanRedeemLoyalityPoints() {
		return canRedeemLoyalityPoints;
	}

	public void setCanRedeemLoyalityPoints(Boolean canRedeemLoyalityPoints) {
		this.canRedeemLoyalityPoints = canRedeemLoyalityPoints;
	}

}
