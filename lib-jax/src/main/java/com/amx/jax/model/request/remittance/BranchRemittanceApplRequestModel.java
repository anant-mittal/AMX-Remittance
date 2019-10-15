package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class BranchRemittanceApplRequestModel extends RemittanceAdditionalBeneFieldModel implements IRemittanceApplicationParams {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3456657697014683661L;
	private String signature;
	private String amlRemarks;
	private BigDecimal serviceMasterId;
	private BigDecimal routingBankId;
	private BigDecimal routingBankBranchId;
	private BigDecimal routingCountryId;
	private BigDecimal remittanceModeId;
	
	@NotNull
	DynamicRoutingPricingDto dynamicRroutingPricingBreakup;
	

	private BigDecimal deliveryModeId;

	public BranchRemittanceApplRequestModel() {
		super();
	}
	
	public BranchRemittanceApplRequestModel(IRemittanceApplicationParams getExchangeRateRequest) {
		this.beneId = getExchangeRateRequest.getBeneficiaryRelationshipSeqIdBD();
		this.routingBankId = getExchangeRateRequest.getCorrespondanceBankIdBD();
		this.deliveryModeId = getExchangeRateRequest.getDeliveryModeIdBD();
		this.foreignAmount = getExchangeRateRequest.getForeignAmountBD();
		this.localAmount = getExchangeRateRequest.getLocalAmountBD();
		this.remittanceModeId = getExchangeRateRequest.getRemitModeIdBD();
		this.serviceMasterId = getExchangeRateRequest.getServiceIndicatorIdBD();
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

	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAmlRemarks() {
		return amlRemarks;
	}
	public void setAmlRemarks(String amlRemarks) {
		this.amlRemarks = amlRemarks;
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
	public void setRemittanceModeId(BigDecimal remittancModeId) {
		this.remittanceModeId = remittancModeId;
	}
	
	public BigDecimal getRoutingCountryId() {
		return routingCountryId;
	}
	public void setRoutingCountryId(BigDecimal routingCountryId) {
		this.routingCountryId = routingCountryId;
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
		// TODO Auto-generated method stub
		return this.deliveryModeId;
	}
	@Override
	@JsonIgnore
	public BigDecimal getRemitModeIdBD() {
		return this.remittanceModeId;
	}
	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}
	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}
	@Override
	public Boolean getAvailLoyalityPoints() {
		return this.availLoyalityPoints;
	}
	@Override
	public ExchangeRateBreakup getExchangeRateBreakup() {
		//return this.branchExRateBreakup;
		return this.dynamicRroutingPricingBreakup.getExRateBreakup();
	}

	public static BranchRemittanceApplRequestModel getInstance(IRemittanceApplicationParams request) {
		BranchRemittanceApplRequestModel model = new BranchRemittanceApplRequestModel();
		model.setAvailLoyalityPoints(request.getAvailLoyalityPoints());
		model.setBeneId(request.getBeneficiaryRelationshipSeqIdBD());
		model.setRoutingBankId(request.getCorrespondanceBankIdBD());
		model.setDeliveryModeId(request.getDeliveryModeIdBD());
		model.setRemittanceModeId(request.getRemitModeIdBD());
		model.setLocalAmount(request.getLocalAmountBD());
		model.setRemittanceModeId(request.getRemitModeIdBD());
		model.setServiceMasterId(request.getServiceIndicatorIdBD());
		return model;
	}

	public BigDecimal getRoutingBankBranchId() {
		return routingBankBranchId;
	}

	public void setRoutingBankBranchId(BigDecimal routingBankBranchId) {
		this.routingBankBranchId = routingBankBranchId;
	}

	public DynamicRoutingPricingDto getDynamicRroutingPricingBreakup() {
		return dynamicRroutingPricingBreakup;
	}

	public void setDynamicRroutingPricingBreakup(DynamicRoutingPricingDto dynamicRroutingPricingBreakup) {
		this.dynamicRroutingPricingBreakup = dynamicRroutingPricingBreakup;
	}

	

	
	

}
