package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.constants.JaxFieldEntity;

public class JaxConditionalFieldRuleDto {
	
	BigDecimal id;
	JaxFieldEntity entityName;
	JaxField field;
	String conditionKey;
	String conditionValue;	
	List<PrefixModel> possibleValues;
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
	public JaxField getField() {
		return field;
	}
	public void setField(JaxField field) {
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
	public List<PrefixModel> getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(List<PrefixModel> possibleValues) {
		this.possibleValues = possibleValues;
	}	

}
