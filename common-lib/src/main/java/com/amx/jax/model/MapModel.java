package com.amx.jax.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.amx.jax.json.JsonSerializerType;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapModel implements JsonSerializerType<Object> {
	protected Map<String, Object> map;

	public MapModel() {
		this.map = new HashMap<String, Object>();
	}

	public MapModel(Map<String, Object> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public MapModel(String json) {
		this.map = JsonUtil.fromJson(json, Map.class);
	}

	public Object get(String key) {
		return this.map.get(key);
	}

	public String getString(String key) {
		return ArgUtil.parseAsString(this.get(key));
	}

	public Long getLong(String key) {
		return ArgUtil.parseAsLong(this.get(key));
	}

	public BigDecimal getBigDecimal(String key) {
		return ArgUtil.parseAsBigDecimal(this.get(key));
	}

	@SuppressWarnings("unchecked")
	public MapModel getMap(String key) {
		return new MapModel((Map<String, Object>) this.get(key));
	}

	@Override
	public Object toObject() {
		return this.map;
	}

	public MapModel fromMap(Map<String, Object> map) {
		this.map = map;
		return this;
	}

	public Map<String, Object> toMap() {
		return this.map;
	}

	public <T> T as(Class<T> clazz) {
		return JsonUtil.getMapper().convertValue(this.map, clazz);
	}

}