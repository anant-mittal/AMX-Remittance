package com.amx.jax;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractModel implements Serializable {

	private static final long serialVersionUID = -5279804951579408228L;

	@JsonIgnore
	public String getModelType() {
		return this.getClass().getName();
	};
}
