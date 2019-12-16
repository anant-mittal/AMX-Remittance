package com.amx.jax.model.request.benebranch;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.client.bene.BeneficiaryConstant.BeneStatus;

public class UpdateBeneStatusRequest {

	@NotNull
	BeneStatus statusCode;
	@NotNull
	Integer beneRelationshipSeqId;
	@NotNull
	BigDecimal reasonCodeId;	

	public BeneStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(BeneStatus statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getBeneRelationshipSeqId() {
		return beneRelationshipSeqId;
	}

	public void setBeneRelationshipSeqId(Integer beneRelationshipSeqId) {
		this.beneRelationshipSeqId = beneRelationshipSeqId;
	}
	public BigDecimal getReasonCodeId() {
		return reasonCodeId;
	}

	public void setReasonCodeId(BigDecimal reasonCodeId) {
		this.reasonCodeId = reasonCodeId;
	}
}
