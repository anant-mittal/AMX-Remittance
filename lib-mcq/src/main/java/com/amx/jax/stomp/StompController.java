package com.amx.jax.stomp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

	@SubscribeMapping("/stomp/tunnel/meta")
	public Map<String, String> meta() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("hey", "ho");
		return map;
	}
}
