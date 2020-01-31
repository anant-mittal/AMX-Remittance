package com.amx.jax.client.bene;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class InstitutionCategoryDto extends ResourceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BigDecimal institutionCategoryId;

	String description;

	public BigDecimal getInstitutionCategoryId() {
		return institutionCategoryId;
	}

	public void setInstitutionCategoryId(BigDecimal institutionCategoryId) {
		this.institutionCategoryId = institutionCategoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public BigDecimal getResourceId() {
		return this.getInstitutionCategoryId();
	}

	@Override
	public String getResourceName() {
		return this.getDescription();
	}

}
