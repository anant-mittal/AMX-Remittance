package com.amx.jax.radar.logger;

import java.util.Map;

import com.amx.jax.model.MapModel;
import com.amx.utils.JsonPath;

public class LoggerMapModel extends MapModel {
	
	private static final JsonPath TYPE = new JsonPath("type");
	

	public LoggerMapModel(Map<String, Object> event) {
		super(event);
	}

	public String getType() {
		
	}
}