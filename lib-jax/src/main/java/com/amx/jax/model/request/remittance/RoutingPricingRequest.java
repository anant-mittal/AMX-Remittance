package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RoutingPricingRequest implements IRemittanceApplicationParams {

	
	@NotNull(message = "benerelation id can not be null")
	BigDecimal beneficiaryRelationshipSeqId;
	BigDecimal localAmount;
	BigDecimal foreignAmount;
	@NotNull(message = "availLoyalityPoints can not be null")
	Boolean availLoyalityPoints;
	Boolean isRoundUp;
	public BigDecimal getBeneficiaryRelationshipSeqId() {
		return beneficiaryRelationshipSeqId;
	}
	public void setBeneficiaryRelationshipSeqId(BigDecimal beneficiaryRelationshipSeqId) {
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
	
	
	@Override
	public Boolean getAvailLoyalityPoints() {
		return availLoyalityPoints;
	}

	public void setAvailLoyalityPoints(Boolean availLoyalityPoints) {
		this.availLoyalityPoints = availLoyalityPoints;
	}
	
	
	public Boolean getIsRoundUp() {
		return isRoundUp;
	}
	public void setIsRoundUp(Boolean isRoundUp) {
		this.isRoundUp = isRoundUp;
	}
	@Override
	@JsonIgnore
	public BigDecimal getBeneficiaryRelationshipSeqIdBD() {
		// TODO Auto-generated method stub
		return this.beneficiaryRelationshipSeqId;
	}
	@Override
	@JsonIgnore
	public BigDecimal getLocalAmountBD() {
		// TODO Auto-generated method stub
		return this.localAmount;
	}
	@Override
	@JsonIgnore
	public BigDecimal getForeignAmountBD() {
		// TODO Auto-generated method stub
		return this.foreignAmount;
	}
	@Override
	@JsonIgnore
	public BigDecimal getCorrespondanceBankIdBD() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@JsonIgnore
	public BigDecimal getServiceIndicatorIdBD() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@JsonIgnore
	public BigDecimal getDeliveryModeIdBD() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@JsonIgnore
	public BigDecimal getRemitModeIdBD() {
		// TODO Auto-generated method stub
		return null;
	}
}
