/**
 * 
 */
package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;

/**
 * @author Prashant
 *
 */
public class RemittanceTransactionResponsetModel extends AbstractModel {

	private BigDecimal equivalentAmount;
	private BigDecimal exchangeRate;
	private BigDecimal txnFee;
	private BigDecimal totalLoyalityPoints;
	private BigDecimal maxLoyalityPointsAvailableForTxn;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.amxlib.model.AbstractModel#getModelType()
	 */
	@Override
	public String getModelType() {
		return "remittance_transaction";
	}

	public BigDecimal getEquivalentAmount() {
		return equivalentAmount;
	}

	public void setEquivalentAmount(BigDecimal equivalentAmount) {
		this.equivalentAmount = equivalentAmount;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
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

}
