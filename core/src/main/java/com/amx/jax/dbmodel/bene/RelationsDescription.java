package com.amx.jax.dbmodel.bene;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_RELATIONS_DESC")
public class RelationsDescription {

	@Id
	@Column(name = "RELATIONS_ID")
	BigDecimal relationsId;

	@Column(name = "LANGUAGE_ID")
	BigDecimal langId;

	@Column(name = "LOCAL_RELATIONS_DESC")
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
