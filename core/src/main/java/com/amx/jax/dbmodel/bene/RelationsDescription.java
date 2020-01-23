package com.amx.jax.dbmodel.bene;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;
import com.amx.utils.ArgUtil;

@Entity
//@Table(name = "EX_RELATIONS_DESC")
@Table(name = "JAX_VW_RELATIONSHIP")
public class RelationsDescription implements IResourceEntity {

	private static String SELF_STR = "3";
	private static String OTHERS_STR = "45";
	private static BigDecimal SELF = new BigDecimal(SELF_STR);
	private static BigDecimal OTHERS = new BigDecimal(OTHERS_STR);

	@Id
	@Column(name = "RELATIONS_ID")
	BigDecimal relationsId;

	@Column(name = "LANGUAGE_ID")
	BigDecimal langId;

	@Column(name = "LOCAL_RELATIONS_DESC")
	String localRelationsDesc;

	@Column(name = "RELATIONS_CODE")
	String relationsCode;
	
	@Column(name = "ISACTIVE")
    String isActive;
	
	@Column(name = "RELEATIONS_DESC_ID")
    BigDecimal realtionsDescId;
	
	@Column(name = "DEFAULT_INDIC")
	String defaultIndic;

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

	@Override
	public BigDecimal resourceId() {
		return relationsId;
	}

	@Override
	public String resourceName() {
		return localRelationsDesc;
	}

	@Override
	public String resourceCode() {
		if (SELF_STR.equals(relationsCode)) {
			return "SELF";
		} else if (OTHERS_STR.equals(relationsCode)) {
			return "OTHERS";
		}
		return ArgUtil.parseAsString(relationsCode);
	}

	@Override
	public String resourceLocalName() {
		return localRelationsDesc;
	}

	public String getRelationsCode() {
		return relationsCode;
	}

	public void setRelationsCode(String relationsCode) {
		this.relationsCode = relationsCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getRealtionsDescId() {
		return realtionsDescId;
	}

	public void setRealtionsDescId(BigDecimal realtionsDescId) {
		this.realtionsDescId = realtionsDescId;
	}

	public String getDefaultIndic() {
		return defaultIndic;
	}

	public void setDefaultIndic(String defaultIndic) {
		this.defaultIndic = defaultIndic;
	}
	
	
	
	
}
