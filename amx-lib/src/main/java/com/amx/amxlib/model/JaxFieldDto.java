package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class JaxFieldDto {

	String name;
	Boolean required;
	String type;
	String defaultValue;
	BigDecimal minLength;
	BigDecimal maxLength;
	List<ValidationRegexDto> validationRegex;
	String label;
	Map<Object, Object> possibleValues;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<ValidationRegexDto> getValidationRegex() {
		return validationRegex;
	}

	public void setValidationRegex(List<ValidationRegexDto> validationRegex) {
		this.validationRegex = validationRegex;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<Object, Object> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(Map<Object, Object> possibleValues) {
		this.possibleValues = possibleValues;
	}

	@Override
	public String toString() {
		return "JaxFieldDto [name=" + name + ", required=" + required + ", type=" + type + ", defaultValue="
				+ defaultValue + ", minLength=" + minLength + ", maxLength=" + maxLength + ", validationRegex="
				+ validationRegex + ", label=" + label + ", possibleValues=" + possibleValues + "]";
	}

}
