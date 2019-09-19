package com.amx.jax.client.bene;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.model.ResourceDTO;

public class BeneficaryStatusDto extends ResourceDTO {
	BigDecimal beneficaryStatusId;
	String beneficaryStatusName;
	Date createdDate;

	public BigDecimal getBeneficaryStatusId() {
		return beneficaryStatusId;
	}

	public void setBeneficaryStatusId(BigDecimal beneficaryStatusId) {
		this.beneficaryStatusId = beneficaryStatusId;
	}

	public String getBeneficaryStatusName() {
		return beneficaryStatusName;
	}

	public void setBeneficaryStatusName(String beneficaryStatusName) {
		this.beneficaryStatusName = beneficaryStatusName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public BigDecimal getResourceId() {
		return this.beneficaryStatusId;
	}

	@Override
	public String getResourceCode() {
		return this.resourceName;
	}
	
	
}
