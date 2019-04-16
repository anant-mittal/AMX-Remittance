package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_FIELD")
public class JaxField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "NAME")
	@Id
	String name;

	@Column(name = "REQUIRED")
	String required;

	@Column(name = "TYPE")
	String type;

	@Column(name = "DEFAULT_VALUE")
	String defaultValue;

	@Column(name = "MIN_LENGTH")
	BigDecimal minLength;

	@Column(name = "MAX_LENGTH")
	BigDecimal maxLength;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "JAX_FIELD_REGEX_MAPPING", joinColumns = {@JoinColumn(name = "FIELD_NAME") }, inverseJoinColumns = { @JoinColumn(name = "REGEX_KEY") })
	List<ValidationRegex> validationRegex;
	
	@Column(name = "LABEL")
	String label;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public BigDecimal getMinLength() {
		return minLength;
	}

	public void setMinLength(BigDecimal minLength) {
		this.minLength = minLength;
	}

	public BigDecimal getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(BigDecimal maxLength) {
		this.maxLength = maxLength;
	}

	public List<ValidationRegex> getValidationRegex() {
		return validationRegex;
	}

	public void setValidationRegex(List<ValidationRegex> validationRegex) {
		this.validationRegex = validationRegex;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
