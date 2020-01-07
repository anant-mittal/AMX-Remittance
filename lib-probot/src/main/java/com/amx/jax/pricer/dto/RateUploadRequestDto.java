package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

public class RateUploadRequestDto implements Serializable {

	private static final long serialVersionUID = -148944277253011363L;

	@NotNull(message = "Application Country Id Can not be Null or Empty")
	private BigDecimal applicationCountryId;

	private List<RateUploadRuleDto> rateUploadRules;

	private Map<String, RATE_UPLOAD_STATUS> ruleStatusUpdateMap;

	@NotNull(message = "Updated By Can not be Null or Empty")
	private String updatedBy;

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public List<RateUploadRuleDto> getRateUploadRules() {
		return rateUploadRules;
	}

	public void setRateUploadRules(List<RateUploadRuleDto> rateUploadRules) {
		this.rateUploadRules = rateUploadRules;
	}

	public Map<String, RATE_UPLOAD_STATUS> getRuleStatusUpdateMap() {
		return ruleStatusUpdateMap;
	}

	public void setRuleStatusUpdateMap(Map<String, RATE_UPLOAD_STATUS> ruleStatusUpdateMap) {
		this.ruleStatusUpdateMap = ruleStatusUpdateMap;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
