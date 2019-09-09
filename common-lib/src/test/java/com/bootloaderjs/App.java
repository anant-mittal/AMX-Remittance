package com.bootloaderjs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.UserClient.AppType;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.tunnel.TunnelMessage;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.TimeUtils;
import com.fasterxml.jackson.core.type.TypeReference;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */

	public static void main(String[] args) {
		Long traceTime = ArgUtil.parseAsLong(ContextUtil.map().get(AppConstants.TRACE_TIME_XKEY), 0L);
		if (traceTime != null && traceTime != 0L) {
			System.out.println(TimeUtils.timeSince(AppContextUtil.getTraceTime()));
		}

	}

	public static void main3(String[] args) {
		String url = "/api/user/tranx/history";
		System.out.println(url.toLowerCase().replace("pub", "b").replace("api", "p").replace("user", "")
				.replace("get", "").replace("post", "").replace("save", "")
				.replace("/", "").replaceAll("[AaEeIiOoUuYyWwHh]", ""));
	}

	public static void main2(String[] args) throws MalformedURLException, URISyntaxException {

		AppContext context = AppContextUtil.getContext();

		UserDeviceClient client = new UserDeviceClient();

		client.setIp("0:0:0:0:0:0:0:1");
		client.setFingerprint("38b3dd46de1d7df8303132bba73ca1e6");
		client.setDeviceType(DeviceType.COMPUTER);
		client.setAppType(AppType.WEB);
		context.setClient(client);
		context.setTraceId("TST-1d59nub55kbgg-1d59nub5827sx");
		context.setTranxId("TST-1d59nub55kbgg-1d59nub5827sx");

		TunnelMessage<Map<String, String>> message = new TunnelMessage<Map<String, String>>(
				new HashMap<String, String>(), context);
		message.setTopic("DATAUPD_CUSTOMER");

		String messageJson = JsonUtil.toJson(message);
		LOGGER.info("====== {}", messageJson);
		TunnelMessage<Map<String, String>> message2 = JsonUtil.fromJson(messageJson,
				new TypeReference<TunnelMessage<Map<String, String>>>() {
				});
		LOGGER.info("====== {}", JsonUtil.toJson(message2));

	}
}
