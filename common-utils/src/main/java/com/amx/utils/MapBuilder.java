package com.amx.utils;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class MapBuilder.
 */
public class MapBuilder {

	/**
	 * Instantiates a new map builder.
	 */
	private MapBuilder() {
		// private constructor to hide the implicit public one.
	}

	/**
	 * The Class MapItem.
	 */
	public static class MapItem {

		/** The key. */
		String key;

		/** The value. */
		Object value;

		/**
		 * Instantiates a new map item.
		 *
		 * @param key
		 *            the key
		 * @param value
		 *            the value
		 */
		MapItem(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * Gets the key.
		 *
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}
	}

	/**
	 * The Class BuilderMap.
	 */
	public static class BuilderMap {

		/** The map. */
		private Map<String, Object> map = new HashMap<String, Object>();

		/**
		 * Instantiates a new builder map.
		 *
		 * @param items
		 *            the items
		 */
		public BuilderMap(MapItem... items) {
			for (MapItem item : items) {
				this.put(item.getKey(), item.getValue());
			}
		}

		/**
		 * Put.
		 *
		 * @param key
		 *            the key
		 * @param value
		 *            the value
		 * @return the builder map
		 */
		public BuilderMap put(String key, Object value) {
			map.put(key, value);
			return this;
		}

		/**
		 * Put.
		 *
		 * @param jsonPath
		 *            the json path
		 * @param value
		 *            the value
		 * @return the builder map
		 */
		public BuilderMap put(JsonPath jsonPath, Object value) {
			jsonPath.save(map, value);
			return this;
		}

		/**
		 * To map.
		 *
		 * @return the map
		 */
		public Map<String, Object> toMap() {
			return map;
		}

		public Map<String, Object> build() {
			return map;
		}

		/**
		 * To json node.
		 *
		 * @return the json node
		 */
		public JsonNode toJsonNode() {
			return JsonUtil.getMapper().valueToTree(map);
		}

	}

	/**
	 * Map.
	 *
	 * @return the builder map
	 */
	public static BuilderMap map() {
		return new BuilderMap();
	}

}
