package com.amx.amxlib.model;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class BeneRelationsDescriptionDto extends ResourceDTO {

	private static final long serialVersionUID = -5304305030408790573L;

	BigDecimal relationsId;

	BigDecimal langId;

	String localRelationsDesc;

	public BigDecimal getRelationsId() {
		return relationsId;
	}

	public void setRelationsId(BigDecimal relationsId) {
		this.relationsId = relationsId;
		//this.resourceId = relationsId;
	}

	public BigDecimal getLangId() {
		return langId;
	}

	public void setLangId(BigDecimal langId) {
		this.langId = langId;
	}

	public String getLocalRelationsDesc() {
		return localRelationsDesc;
	}

	public void setLocalRelationsDesc(String localRelationsDesc) {
		this.localRelationsDesc = localRelationsDesc;
	}

}
