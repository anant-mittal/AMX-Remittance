package com.amx.amxlib.model;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ValidationRegexDto {

	@ApiMockModelProperty(example = "ADDRESS")
	String key;

	@ApiMockModelProperty(example = "[a-zA-Z ,.'-:\\\"\\\"]+")
	String value;

	@ApiMockModelProperty(example = "Alphanumeric and ,.'-:\\\"\\\"")
	String description;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
