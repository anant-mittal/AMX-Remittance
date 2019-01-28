package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.FlexFieldDto;


public class BranchRemittanceApplRequestModel {
	
	private BigDecimal relationshipId;
	private BigDecimal sourceOfFund;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private boolean availLoyalityPoints;
	private BigDecimal additionalBankRuleFiledId;
	private BigDecimal srlId;
	private Map<String, Object> flexFields;
	private BigDecimal domXRate;
	@NotNull
	private BranchExchangeRateBreakup branchExRateBreakup;
	public Map<String, FlexFieldDto> flexFieldDtoMap;
	private String signature;
	private String amlRemarks;

	
	
	
	public BigDecimal getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(BigDecimal relationshipId) {
		this.relationshipId = relationshipId;
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
	public BigDecimal getAdditionalBankRuleFiledId() {
		return additionalBankRuleFiledId;
	}
	public void setAdditionalBankRuleFiledId(BigDecimal additionalBankRuleFiledId) {
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
	}
	public BigDecimal getSrlId() {
		return srlId;
	}
	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}
	
	
	public Map<String, Object> getFlexFields() {
		return flexFields;
	}
	public void setFlexFields(Map<String, Object> flexFields) {
		this.flexFields = flexFields;
	}
	public BigDecimal getDomXRate() {
		return domXRate;
	}
	public void setDomXRate(BigDecimal domXRate) {
		this.domXRate = domXRate;
	}
	public BranchExchangeRateBreakup getBranchExRateBreakup() {
		return branchExRateBreakup;
	}
	public void setBranchExRateBreakup(BranchExchangeRateBreakup branchExRateBreakup) {
		this.branchExRateBreakup = branchExRateBreakup;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Map<String, FlexFieldDto> getFlexFieldDtoMap() {
		return flexFieldDtoMap;
	}
	public void setFlexFieldDtoMap(Map<String, FlexFieldDto> flexFieldDtoMap) {
		this.flexFieldDtoMap = flexFieldDtoMap;
	}
	public String getAmlRemarks() {
		return amlRemarks;
	}
	public void setAmlRemarks(String amlRemarks) {
		this.amlRemarks = amlRemarks;
	}


}
