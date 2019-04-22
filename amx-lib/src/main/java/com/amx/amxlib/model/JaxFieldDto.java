package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.swagger.ApiMockModelProperty;

public class JaxFieldDto {

	@ApiMockModelProperty(value = "Name of param", example = "BENE_HOUSE_NO")
	String name;

	@ApiMockModelProperty(value = "If params is mandatory", example = "true")
	Boolean required;

	@ApiMockModelProperty(value = "Type of param", example = "TEXT")
	String type;

	@ApiMockModelProperty(value = "Default value of param", example = "null")
	String defaultValue;

	@ApiMockModelProperty(value = "Minimum lenght of validation", example = "1")
	BigDecimal minLength;

	@ApiMockModelProperty(value = "Maximum lenght of validation", example = "100")
	BigDecimal maxLength;

	List<ValidationRegexDto> validationRegex;

	@ApiMockModelProperty(value = "Label", example = "House No")
	String label;

	List<JaxFieldValueDto> possibleValues;

	@ApiMockModelProperty(value = "json path of param in request", example = "additionalFields.BENE_HOUSE_NO")
	String dtoPath;

	Map<String, Object> additionalValidations = new HashMap<>();

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

	@Override
	public String toString() {
		return "JaxFieldDto [name=" + name + ", required=" + required + ", type=" + type + ", defaultValue="
				+ defaultValue + ", minLength=" + minLength + ", maxLength=" + maxLength + ", validationRegex="
				+ validationRegex + ", label=" + label + ", possibleValues=" + possibleValues + "]";
	}

	public List<JaxFieldValueDto> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<JaxFieldValueDto> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public String getDtoPath() {
		return dtoPath;
	}

	public void setDtoPath(String dtoPath) {
		this.dtoPath = dtoPath;
	}

	public Map<String, Object> getAdditionalValidations() {
		return additionalValidations;
	}

	public void setAdditionalValidations(Map<String, Object> additionalValidations) {
		this.additionalValidations = additionalValidations;
	}

}
