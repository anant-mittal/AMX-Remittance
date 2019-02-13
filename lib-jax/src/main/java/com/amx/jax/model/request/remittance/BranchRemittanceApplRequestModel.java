package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class BranchRemittanceApplRequestModel implements IRemittanceApplicationParams {
	
	private BigDecimal beneId;
	private BigDecimal sourceOfFund;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private boolean availLoyalityPoints;
	private BigDecimal additionalBankRuleFiledId;
	private Map<String, Object> flexFields;
	private BigDecimal domXRate;
	@NotNull
	private BranchExchangeRateBreakup branchExRateBreakup;
	public Map<String, FlexFieldDto> flexFieldDtoMap;
	private String signature;
	private String amlRemarks;
	private BigDecimal serviceMasterId;
	private BigDecimal routingBankId;
	private BigDecimal routingCountryId;
	private BigDecimal remittanceModeId;
	private BigDecimal purposeOfTrnxId;
	private BigDecimal deliveryModeId;

	

	
	
	
	
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

	public void populateFlexFieldDtoMap() {
		if (this.flexFields != null) {
			Map<String, String> flexFieldMap = createFlexFieldMap(flexFields);
			Function<Map.Entry<String, String>, FlexFieldDto> valueMapper = (entryObject) -> {
				String value = entryObject.getValue().toString();
				FlexFieldDto flexFieldDto = null;
				try {
					flexFieldDto = JsonUtil.fromJson(value, FlexFieldDto.class);
				} catch (Exception e) {
				}
				if (flexFieldDto == null) {
					flexFieldDto = new FlexFieldDto(value);
				}
				return flexFieldDto;
			};
			this.flexFieldDtoMap = flexFieldMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, valueMapper));
		}
	}

	private Map<String, String> createFlexFieldMap(Map<String, Object> flexFields2) {

		Set<Entry<String, Object>> es = flexFields2.entrySet();
		Map<String, String> output = es.stream().collect(Collectors.toMap(x -> x.getKey(), x -> JsonUtil.toJson(x.getValue())));
		return output;
	}
	public BigDecimal getBeneId() {
		return beneId;
	}
	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
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
	public BigDecimal getPurposeOfTrnxId() {
		return purposeOfTrnxId;
	}
	public void setPurposeOfTrnxId(BigDecimal purposeOfTrnxId) {
		this.purposeOfTrnxId = purposeOfTrnxId;
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

}
