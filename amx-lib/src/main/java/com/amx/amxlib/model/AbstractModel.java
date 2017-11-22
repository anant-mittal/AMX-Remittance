package com.amx.amxlib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractModel {

	@JsonIgnore
	public abstract String getModelType();
}
