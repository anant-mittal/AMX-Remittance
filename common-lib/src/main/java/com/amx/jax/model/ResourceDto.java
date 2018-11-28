package com.amx.jax.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Prashant
 *
 */
public class ResourceDto {

	/**
	 * db identifier of resource
	 */
	@JsonProperty("_id")
	Number id;

	/**
	 * name of resource
	 */
	Object name;

	public ResourceDto(Number id, String name) {
		this.id = id;
		this.name = name;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

}
