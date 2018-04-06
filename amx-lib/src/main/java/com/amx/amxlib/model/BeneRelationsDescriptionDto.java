package com.amx.amxlib.model;

import java.math.BigDecimal;

public class BeneRelationsDescriptionDto {

	BigDecimal relationsId;

	BigDecimal langId;

	String localRelationsDesc;

	public BigDecimal getRelationsId() {
		return relationsId;
	}

	public void setRelationsId(BigDecimal relationsId) {
		this.relationsId = relationsId;
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
