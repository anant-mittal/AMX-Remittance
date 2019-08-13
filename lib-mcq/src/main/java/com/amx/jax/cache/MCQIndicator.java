package com.amx.jax.cache;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.def.IndicatorListner;
import com.amx.jax.tunnel.TunnelSubscriberFactory;
import com.amx.utils.ArgUtil;

@Component
public class MCQIndicator implements IndicatorListner {

	@Autowired
	TunnelSubscriberFactory tunnelSubscriberFactory;
	public static final Map<String, Object> statusMap = Collections.synchronizedMap(new HashMap<String, Object>());

	@Autowired
	SampleTestCacheBox sampleTestCacheBox;

	@Autowired
	AppConfig appConfig;

	public static String TIMER = null;

	@Override
	public Map<String, Object> getIndicators() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tunnel.listner", MCQIndicator.getStatus());

		Map<String, Object> cacheMap = new HashMap<String, Object>();

		if (ArgUtil.isEmpty(TIMER)) {
			TIMER = appConfig.getSpringAppName() + System.currentTimeMillis();
		}

		try {
			sampleTestCacheBox.get(TIMER);
			cacheMap.put("GET", "PASS");
		} catch (Exception e) {
			cacheMap.put("GET", "FAIL");
		}

		try {
			sampleTestCacheBox.remove(TIMER);
			cacheMap.put("DEL", "PASS");
		} catch (Exception e) {
			cacheMap.put("DEL", "FAIL");
		}

		TIMER = appConfig.getSpringAppName() + System.currentTimeMillis();

		try {
			sampleTestCacheBox.put(TIMER, "PASS");
			cacheMap.put("PUT", "PASS");
		} catch (Exception e) {
			cacheMap.put("PUT", "FAIL");
		}

		map.put("redis.cache", cacheMap);

		return map;
	}

	public static void messageException(String channel) {
		statusMap.put("channel." + channel + ".excep.ts", TunnelSubscriberFactory.TS_FORMAT.format(new Date()));
	}

	public static void messageIgnored(String channel) {
		statusMap.put("channel." + channel + ".ignrd.ts", TunnelSubscriberFactory.TS_FORMAT.format(new Date()));
	}

	public static void messageProcessed(String channel) {
		statusMap.put("channel." + channel + ".prcsd.ts", TunnelSubscriberFactory.TS_FORMAT.format(new Date()));
	}

	public static void messageRcvd(String channel) {
		statusMap.put("channel." + channel + ".rcvd.ts", TunnelSubscriberFactory.TS_FORMAT.format(new Date()));
	}

	public static Map<String, Object> getStatus() {
		return statusMap;
	}

	public static String messageSubscribed(String channel) {
		statusMap.put("channel." + channel + ".sub.ts", TunnelSubscriberFactory.TS_FORMAT.format(new Date()));
		return channel;
	}

}
