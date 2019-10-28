package com.amx.jax.logger;

import java.util.Map;

import com.amx.jax.model.MapModel;
import com.amx.utils.JsonPath;

public class AuditMapModel extends MapModel {

	private static final JsonPath TYPE = new JsonPath("type");

	public AuditMapModel(Map<String, Object> event) {
		super(event);
	}

	public String getType() {
		return TYPE.load(this.map, null);
	}
}