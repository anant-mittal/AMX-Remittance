package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

public class RateUploadRequestDto implements Serializable {

	private static final long serialVersionUID = -148944277253011363L;

	private List<RateUploadRuleDto> rateUploadRules;

	private Map<String, RATE_UPLOAD_STATUS> ruleStatusUpdateMap;

	private String updatedBy;

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
