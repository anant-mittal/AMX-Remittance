package com.amx.jax.auth;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonUtil;

public final class RuntimeTest {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		System.out.println(" ======== String Test ======= " + "Y".equalsIgnoreCase(null));

		Map<String, Object> innerJsonMap = new HashMap<String, Object>();

		innerJsonMap.put("1", "value1");
		innerJsonMap.put("2", "value2");
		innerJsonMap.put("3", "value3");
		innerJsonMap.put("4", "value4");
		innerJsonMap.put("5", "value5");
		innerJsonMap.put("6", "value6");
		innerJsonMap.put("7", "value7");

		Map<String, Map<String, Object>> outMap = new HashMap<String, Map<String, Object>>();

		outMap.put("map1", innerJsonMap);
		outMap.put("map2", innerJsonMap);
		outMap.put("map3", innerJsonMap);
		outMap.put("map4", innerJsonMap);
		outMap.put("map5", innerJsonMap);

		System.out.println(" JSON ==> " + JsonUtil.toJson(outMap));

		Map<String, Map<String, Object>> revJsonMap = (Map<String, Map<String, Object>>) JsonUtil
				.fromJson(JsonUtil.toJson(outMap), Map.class);

		System.out.println(" Rev MAP ==> " + JsonUtil.toJson(revJsonMap));

		List<String> list = new ArrayList<String>();

		list.add("value1");
		list.add("value2");
		list.add("value3");
		list.add("value4");
		list.add("value5");
		list.add("value6");
		list.add("value7");
		list.add("value8");

		String jsonL = JsonUtil.toJson(list);

		System.out.println(" List Json ==> " + jsonL);

		System.out.println(" From Json ==>" + JsonUtil.fromJson(jsonL, List.class));

		String devicePairTokenStr = CryptoUtil.getSHA2Hash( "HFOSQUZNXGGNF" + Long.toString(10l));

		System.out.println(" Device Pair Token ==> " + devicePairTokenStr);

	}

}
