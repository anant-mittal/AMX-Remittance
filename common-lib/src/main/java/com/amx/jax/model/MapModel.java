package com.amx.jax.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amx.jax.json.JsonSerializerType;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapModel implements JsonSerializerType<Object> {

	public static class MapEntry {
		private Object value;

		public MapEntry(Object value) {
			this.value = value;
		}

		public String asString() {
			return ArgUtil.parseAsString(value);
		}

		public String asString(String defaultvalue) {
			return ArgUtil.parseAsString(value, defaultvalue);
		}

		public Long asLong() {
			return ArgUtil.parseAsLong(value);
		}

		public Long asLong(Long defaultvalue) {
			return ArgUtil.parseAsLong(value, defaultvalue);
		}

		public BigDecimal asBigDecimal() {
			return ArgUtil.parseAsBigDecimal(value);
		}

		public BigDecimal asBigDecimal(BigDecimal defaultvalue) {
			return ArgUtil.parseAsBigDecimal(value, defaultvalue);
		}

		@SuppressWarnings("unchecked")
		public <T> List<T> asList(T listItem) {
			return ArgUtil.parseAsListOfT(value, listItem, ((List<T>) Constants.EMPTY_LIST), false);
		}

	}

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

	public MapEntry entry(String key) {
		return new MapEntry(this.map.get(key));
	}

	public MapEntry first() {
		return new MapEntry(this.getFirst());
	}

	public Object get(String key) {
		return this.map.get(key);
	}

	public Object getFirst() {
		for (Entry<String, Object> iterable_element : map.entrySet()) {
			return iterable_element.getValue();
		}
		return null;
	}

	public String getString(String key) {
		return ArgUtil.parseAsString(this.get(key));
	}

	public String getString(String key, String defaultvalue) {
		return ArgUtil.parseAsString(this.get(key), defaultvalue);
	}

	public Long getLong(String key) {
		return ArgUtil.parseAsLong(this.get(key));
	}

	public Long getLong(String key, Long defaultvalue) {
		return ArgUtil.parseAsLong(this.get(key), defaultvalue);
	}

	public BigDecimal getBigDecimal(String key) {
		return ArgUtil.parseAsBigDecimal(this.get(key));
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultvalue) {
		return ArgUtil.parseAsBigDecimal(this.get(key), defaultvalue);
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