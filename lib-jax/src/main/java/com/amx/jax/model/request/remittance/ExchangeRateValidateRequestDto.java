package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExchangeRateValidateRequestDto implements IRemittanceApplicationParams {

	private BigDecimal beneId;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private BigDecimal serviceMasterId;
	private BigDecimal routingBankId;
	private BigDecimal remittanceModeId;
	private BigDecimal deliveryModeId;
	private Boolean availLoyalityPoints;

	public BigDecimal getBeneId() {
		return beneId;
	}

	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
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

	public BigDecimal getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(BigDecimal serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}

	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}

	public BigDecimal getRemittanceModeId() {
		return remittanceModeId;
	}

	public void setRemittanceModeId(BigDecimal remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	public void setAvailLoyalityPoints(Boolean availLoyalityPoints) {
		this.availLoyalityPoints = availLoyalityPoints;
	}

	@Override
	@JsonIgnore
	public BigDecimal getBeneficiaryRelationshipSeqIdBD() {
		return this.beneId;
	}

	@Override
	@JsonIgnore
	public BigDecimal getLocalAmountBD() {
		return this.localAmount;
	}

	@Override
	@JsonIgnore
	public BigDecimal getForeignAmountBD() {
		return this.foreignAmount;
	}

	@Override
	@JsonIgnore
	public BigDecimal getCorrespondanceBankIdBD() {
		return this.routingBankId;
	}

	@Override
	@JsonIgnore
	public BigDecimal getServiceIndicatorIdBD() {
		return this.serviceMasterId;
	}

	@Override
	@JsonIgnore
	public BigDecimal getDeliveryModeIdBD() {
		return this.deliveryModeId;
	}

	@Override
	@JsonIgnore
	public BigDecimal getRemitModeIdBD() {
		return this.remittanceModeId;
	}

	@Override
	public Boolean getAvailLoyalityPoints() {
		return this.availLoyalityPoints;
	}

}