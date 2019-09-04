package com.amx.jax.dbmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VALIDATION_REGEX")
public class ValidationRegex {

	@Id
	@Column(name = "KEY")
	String key;

	@Column(name = "VALUE")
	String value;

	@Column(name = "DESCRIPTION")
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
