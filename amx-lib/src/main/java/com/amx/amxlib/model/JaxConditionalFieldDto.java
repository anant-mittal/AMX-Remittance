package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.List;

import com.amx.amxlib.constant.JaxFieldEntity;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;

public class JaxConditionalFieldDto {

	BigDecimal id;

	JaxFieldEntity entityName;

	JaxFieldDto field;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public JaxFieldEntity getEntityName() {
		return entityName;
	}

	public void setEntityName(JaxFieldEntity entityName) {
		this.entityName = entityName;
	}

	public JaxFieldDto getField() {
		return field;
	}

	public void setField(JaxFieldDto field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return "JaxConditionalFieldDto [id=" + id + ", entityName=" + entityName + ", field=" + field + "]";
	}
}
