package com.amx.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlatMap {

	private static final Map<String, JsonPath> jsonpaths = new ConcurrentHashMap<String, JsonPath>();

	private Map<String, Object> map = null;

	public FlatMap(Map<String, Object> tempmap) {
		this.map = tempmap;
	}

	public JsonPath getJsonPath(String key) {
		JsonPath jsonPath = jsonpaths.getOrDefault(key, null);
		if (jsonPath == null) {
			jsonPath = new JsonPath(key);
			jsonpaths.put(key, jsonPath);
		}
		return jsonPath;
	}

	public String get(String key) {
		JsonPath jsonPath = getJsonPath(key);
		return jsonPath.load(this.map, Constants.BLANK);
	}

	public Integer getInteger(String key) {
		JsonPath jsonPath = getJsonPath(key);
		return jsonPath.load(this.map, 0);
	}

	public FlatMap getFlatMap(String key) {
		JsonPath jsonPath = getJsonPath(key);
		return new FlatMap(jsonPath.load(this.map, null));
	}

	public List<FlatMap> getList(String key) {
		JsonPath jsonPath = getJsonPath(key);
		List<Map<String, Object>> list = jsonPath.loadList(this.map, null);
		List<FlatMap> list2 = new ArrayList<FlatMap>();
		for (Map<String, Object> item : list) {
			list2.add(new FlatMap(item));
		}
		return list2;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
}
