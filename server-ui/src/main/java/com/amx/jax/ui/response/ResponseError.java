package com.amx.jax.ui.response;

import java.io.Serializable;

public class ResponseError implements Serializable {

	private static final long serialVersionUID = -3253269120058295762L;
	String obzect = null;

	public String getObzect() {
		return obzect;
	}

	public void setObzect(String obzect) {
		this.obzect = obzect;
	}

	String field = null;
	String description = null;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
