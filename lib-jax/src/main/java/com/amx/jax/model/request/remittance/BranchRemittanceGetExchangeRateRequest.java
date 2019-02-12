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
public class BranchRemittanceGetExchangeRateRequest implements IRemittanceApplicationParams {

	@NotNull(message = "bene id can not be null")
	Long beneficiaryRelationshipSeqId;
	BigDecimal localAmount;
	BigDecimal foreignAmount;
	@NotNull(message = "correspondanceBankId can not be null")
	Long correspondanceBankId;
	@NotNull(message = "serviceIndicatorId can not be null")
	Long serviceIndicatorId;
	@NotNull(message = "remittanceModeId can not be null")
	Long remittanceModeId;
	@NotNull(message = "deliveryModeId can not be null")
	Long deliveryModeId;

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

	@Override
	public BigDecimal getBeneficiaryRelationshipSeqIdBD() {
		return BigDecimal.valueOf(this.beneficiaryRelationshipSeqId);
	}

	@Override
	public BigDecimal getLocalAmountBD() {
		return this.localAmount;
	}

	@Override
	public BigDecimal getForeignAmountBD() {
		return this.foreignAmount;
	}

	@Override
	public BigDecimal getCorrespondanceBankIdBD() {
		return BigDecimal.valueOf(this.correspondanceBankId);
	}

	@Override
	public BigDecimal getServiceIndicatorIdBD() {
		return BigDecimal.valueOf(this.serviceIndicatorId);
	}

	@Override
	public BigDecimal getDeliveryModeIdBD() {
		return BigDecimal.valueOf(this.deliveryModeId);
	}

	@Override
	public BigDecimal getRemitModeIdBD() {
		return BigDecimal.valueOf(this.remittanceModeId);
	}

	public Long getRemittanceModeId() {
		return remittanceModeId;
	}

	public void setRemittanceModeId(Long remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	public Long getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(Long deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}
}
