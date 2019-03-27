package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ExchangeRateBreakup;

public abstract class AbstractRemittanceApplicationRequestModel extends AbstractModel{

	protected BigDecimal sourceOfFund;
	protected BigDecimal localAmount;
	protected BigDecimal foreignAmount;
	protected boolean availLoyalityPoints;
	protected BigDecimal domXRate;
	
	/**
	 * @return exiting exrate breakup
	 */
	public abstract ExchangeRateBreakup getExchangeRateBreakup();

	public BigDecimal getSourceOfFund() {
		return sourceOfFund;
	}

	public void setSourceOfFund(BigDecimal sourceOfFund) {
		this.sourceOfFund = sourceOfFund;
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

	public boolean isAvailLoyalityPoints() {
		return availLoyalityPoints;
	}

	public void setAvailLoyalityPoints(boolean availLoyalityPoints) {
		this.availLoyalityPoints = availLoyalityPoints;
	}
	
	public BigDecimal getDomXRate() {
		return domXRate;
	}

	public void setDomXRate(BigDecimal domXRate) {
		this.domXRate = domXRate;
	}
}
