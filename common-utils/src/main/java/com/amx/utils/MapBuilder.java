package com.amx.utils;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class MapBuilder {

	private MapBuilder() {
		// private constructor to hide the implicit public one.
	}

	public static class MapItem {
		String key;
		Object value;

		MapItem(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}
	}

	public static class BuilderMap {
		private Map<String, Object> map = new HashMap<String, Object>();

		public BuilderMap(MapItem... items) {
			for (MapItem item : items) {
				this.put(item.getKey(), item.getValue());
			}
		}

		public BuilderMap put(String key, Object value) {
			map.put(key, value);
			return this;
		}

		public BuilderMap put(JsonPath jsonPath, Object value) {
			jsonPath.save(map, value);
			return this;
		}

		public Map<String, Object> toMap() {
			return map;
		}

		public JsonNode toJsonNode() {
			return JsonUtil.getMapper().valueToTree(map);
		}

	}

	public static BuilderMap map() {
		return new BuilderMap();
	}

}
