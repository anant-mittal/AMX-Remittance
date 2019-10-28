package com.amx.jax.logger;

import java.math.BigDecimal;
import java.util.Map;

import com.amx.jax.model.MapModel;
import com.amx.utils.JsonPath;

public class AuditMapModel extends MapModel {

	private static final JsonPath TYPE = new JsonPath("type");
	private static final JsonPath TARGET_ID = new JsonPath("targetId");

	public AuditMapModel(Map<String, Object> event) {
		super(event);
	}

	public String getType() {
		return TYPE.load(this.map, null);
	}

	public BigDecimal getTargetId() {
		return this.entry(TARGET_ID).asBigDecimal();
	}
}