/**
 * 
 */
package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;
import com.amx.amxlib.constant.LoyalityPointState;
import com.amx.amxlib.model.AbstractModel;

/**
 * @author Prashant
 *
 */
public class RemittanceTransactionResponsetModel extends AbstractModel {

	private BigDecimal txnFee;
	private BigDecimal totalLoyalityPoints;
	private BigDecimal maxLoyalityPointsAvailableForTxn;
	private ExchangeRateBreakup exRateBreakup;
	private Boolean canRedeemLoyalityPoints;
	private LoyalityPointState loyalityPointState;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.amxlib.model.AbstractModel#getModelType()
	 */
	@Override
	public String getModelType() {
		return "remittance_transaction";
	}

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

	public LoyalityPointState getLoyalityPointState() {
		return loyalityPointState;
	}

	public void setLoyalityPointState(LoyalityPointState loyalityPointState) {
		this.loyalityPointState = loyalityPointState;
	}

}
