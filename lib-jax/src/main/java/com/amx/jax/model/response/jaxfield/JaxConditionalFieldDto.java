package com.amx.jax.model.response.jaxfield;

import java.math.BigDecimal;

public class JaxConditionalFieldDto {

	BigDecimal id;

	JaxFieldEntity entityName;

	JaxFieldDto field;
	
	public JaxConditionalFieldDto() {
		super();
	}

	public JaxConditionalFieldDto(JaxFieldDto field) {
		super();
		this.field = field;
	}

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
