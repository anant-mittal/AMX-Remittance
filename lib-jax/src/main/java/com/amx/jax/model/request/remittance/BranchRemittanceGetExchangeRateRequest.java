/**
 * 
 */
package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

/**
 * @author Prashant
 *
 */
public class BranchRemittanceGetExchangeRateRequest {

	@NotNull(message = "bene id can not be null")
	Long beneficiaryRelationshipSeqId;
	BigDecimal localAmount;
	BigDecimal foreignAmount;
	@NotNull(message = "correspondanceBankId can not be null")
	Long correspondanceBankId;
	@NotNull(message = "serviceIndicatorId can not be null")
	Long serviceIndicatorId;

	public Long getBeneficiaryRelationshipSeqId() {
		return beneficiaryRelationshipSeqId;
	}

	public void setBeneficiaryRelationshipSeqId(Long beneficiaryRelationshipSeqId) {
		this.beneficiaryRelationshipSeqId = beneficiaryRelationshipSeqId;
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

	public Long getCorrespondanceBankId() {
		return correspondanceBankId;
	}

	public void setCorrespondanceBankId(Long correspondanceBankId) {
		this.correspondanceBankId = correspondanceBankId;
	}

	public Long getServiceIndicatorId() {
		return serviceIndicatorId;
	}

	public void setServiceIndicatorId(Long serviceIndicatorId) {
		this.serviceIndicatorId = serviceIndicatorId;
	}

	@Override
	public String toString() {
		return "BranchRemittanceGetExchangeRateRequest [beneficiaryRelationshipSeqId=" + beneficiaryRelationshipSeqId
				+ ", localAmount=" + localAmount + ", foreignAmount=" + foreignAmount + ", correspondanceBankId="
				+ correspondanceBankId + ", serviceIndicatorId=" + serviceIndicatorId + "]";
	}
}
