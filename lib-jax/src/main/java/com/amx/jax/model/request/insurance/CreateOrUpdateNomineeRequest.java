package com.amx.jax.model.request.insurance;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CreateOrUpdateNomineeRequest {

	@ApiMockModelProperty(example = "4312838")
	BigDecimal beneRelationshipSeqId;

	@Min(value = 1)
	@Max(value = 100)
	@ApiMockModelProperty(example = "10")
	Integer percentage;

	@ApiMockModelProperty(example = "2")
	BigDecimal nomineeId;

	public BigDecimal getBeneRelationshipSeqId() {
		return beneRelationshipSeqId;
	}

	public void setBeneRelationshipSeqId(BigDecimal beneRelationshipSeqId) {
		this.beneRelationshipSeqId = beneRelationshipSeqId;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getNomineeId() {
		return nomineeId;
	}

	public void setNomineeId(BigDecimal nomineeId) {
		this.nomineeId = nomineeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beneRelationshipSeqId == null) ? 0 : beneRelationshipSeqId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateOrUpdateNomineeRequest other = (CreateOrUpdateNomineeRequest) obj;
		if (beneRelationshipSeqId == null) {
			if (other.beneRelationshipSeqId != null)
				return false;
		} else if (!beneRelationshipSeqId.equals(other.beneRelationshipSeqId))
			return false;
		return true;
	}

}
