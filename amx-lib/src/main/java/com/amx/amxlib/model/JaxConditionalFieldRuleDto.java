package com.amx.amxlib.model;

import java.math.BigDecimal;

import com.amx.amxlib.constant.JaxFieldEntity;

public class JaxConditionalFieldRuleDto {

	BigDecimal id;

	JaxFieldEntity entityName;

	JaxFieldDto field;

	String conditionKey;

	String conditionValue;

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

	public String getConditionKey() {
		return conditionKey;
	}

	public void setConditionKey(String conditionKey) {
		this.conditionKey = conditionKey;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
}
