package com.amx.jax.auth; 

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.json.GsonJsonParser;

import com.amx.utils.JsonUtil;

public final class RuntimeTest {

	public static void main(String[] args) {
		
		System.out.println(" ======== String Test ======= " + "Y".equalsIgnoreCase(null));
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		jsonMap.put("1", "value1");
		jsonMap.put("2", "value2");
		jsonMap.put("3", "value3");
		jsonMap.put("4", "value4");
		jsonMap.put("5", "value5");
		jsonMap.put("6", "value6");
		jsonMap.put("7", "value7");
		
		
		System.out.println(" JSON ==> " + JsonUtil.toJson(jsonMap));  

		Map<String, Object> revJsonMap = JsonUtil.toMap(JsonUtil.toJson(jsonMap));
		
		System.out.println(" MAP ==> " + revJsonMap.values());
		
		
		
	}

}
