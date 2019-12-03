package com.amx.jax.dbmodel.bene;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_INSTITUTION_CAT_MASTER")
public class InstitutionCategoryMaster {

	@Id
	@Column(name = "INSTITUTION_ID")
	BigDecimal institutionCategoryId;

	@Column(name = "DESCRIPTION")
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

}
