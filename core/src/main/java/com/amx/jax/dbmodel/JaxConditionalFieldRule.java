package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.amx.jax.constants.JaxFieldEntity;

@Entity
@Table(name = "JAX_CONDITIONAL_FIELD_RULE")
public class JaxConditionalFieldRule {

	@Column(name = "JAX_COND_FIELD_RULE_SEQ_ID")
	@Id
	BigDecimal id;

	@Column(name = "ENTITY_NAME")
	@Enumerated(EnumType.STRING)
	JaxFieldEntity entityName;

	@JoinColumn(name = "FIELD_NAME")
	@ManyToOne
	JaxField field;

	@Column(name = "CONDITION_KEY")
	String conditionKey;

	@Column(name = "CONDITION_VALUE")
	String conditionValue;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
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

	public JaxFieldEntity getEntityName() {
		return entityName;
	}

	public void setEntityName(JaxFieldEntity entityName) {
		this.entityName = entityName;
	}

}
