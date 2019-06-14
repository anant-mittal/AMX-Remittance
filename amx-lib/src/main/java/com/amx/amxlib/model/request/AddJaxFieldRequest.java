package com.amx.amxlib.model.request;

import java.math.BigDecimal;
import java.util.List;

import com.amx.libjax.model.jaxfield.ValidationRegexDto;

public class AddJaxFieldRequest {

	String name;

	String required;

	String type;

	String defaultValue;

	BigDecimal minLength;

	BigDecimal maxLength;

	List<ValidationRegexDto> validationRegex;

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

	public List<ValidationRegexDto> getValidationRegex() {
		return validationRegex;
	}

	public void setValidationRegex(List<ValidationRegexDto> validationRegex) {
		this.validationRegex = validationRegex;
	}

}
