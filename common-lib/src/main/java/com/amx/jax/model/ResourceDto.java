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
	BigDecimal id;

	/**
	 * name of resource
	 */
	Object name;

	public ResourceDto(){
		
	}
	
	public ResourceDto(BigDecimal id, String name) {
		this.id = id;
		this.name = name;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

}
