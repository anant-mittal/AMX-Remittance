/**
 * 
 */
package com.amx.amxlib.model.request;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;

/**
 * @author Prashant
 *
 */
public class RemittanceTransactionRequestModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal beneId;
	private BigDecimal sourceOfFund;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private boolean availLoyalityPoints;
	private BigDecimal additionalBankRuleFiledId;
	private BigDecimal srlId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.amxlib.model.AbstractModel#getModelType()
	 */
	@Override
	public String getModelType() {
		return "remittance_transaction";
	}

	public BigDecimal getBeneId() {
		return beneId;
	}

	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
	}

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

	@Override
	public String toString() {
		return "RemittanceTransactionRequestModel [beneId=" + beneId + ", sourceOfFund=" + sourceOfFund
				+ ", localAmount=" + localAmount + ", foreignAmount=" + foreignAmount 
				+ ", availLoyalityPoints=" + availLoyalityPoints + "]";
	}

	public BigDecimal getSrlId() {
		return srlId;
	}

	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}

	public BigDecimal getAdditionalBankRuleFiledId() {
		return additionalBankRuleFiledId;
	}

	public void setAdditionalBankRuleFiledId(BigDecimal additionalBankRuleFiledId) {
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
	}

}
