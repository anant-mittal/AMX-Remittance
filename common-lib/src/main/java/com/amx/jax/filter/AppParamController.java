package com.amx.jax.filter;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppParam;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.model.UserDevice;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.types.DigitsDnum;
import com.amx.jax.types.Pnum;
import com.amx.jax.types.WritersPnum;
import com.amx.utils.CryptoUtil.HashBuilder;

@RestController
public class AppParamController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppParamController.class);
	public static final String PUB_AMX_PREFIX = "/pub/amx";
	public static final String PUBG_AMX_PREFIX = "/pubg/";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";

	static {
		// Pnum.readEnums();
		Pnum.init(WritersPnum.class);
	}

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@ApiRequest(type = RequestType.PING)
	@RequestMapping(value = PARAM_URL, method = RequestMethod.GET)
	public AppParam[] geoLocation(@RequestParam(required = false) AppParam id) {
		if (id != null) {
			id.setEnabled(!id.isEnabled());
			LOGGER.info("App Param {} changed to {}", id, id.isEnabled());
		}
		return AppParam.values();
	}

	@RequestMapping(value = "/pub/amx/device", method = RequestMethod.GET)
	public AmxApiResponse<UserDevice, Map<String, Object>> userDevice() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TenantContextHolder.TENANT, TenantContextHolder.currentSite(false));
		AmxApiResponse<UserDevice, Map<String, Object>> resp = new AmxApiResponse<UserDevice, Map<String, Object>>();
		resp.setMeta(map);
		resp.setData(commonHttpRequest.getUserDevice());
		return resp;
	}

	@RequestMapping(value = "/pub/amx/pnum", method = RequestMethod.GET)
	public WritersPnum geoLocation(@RequestParam(required = false) WritersPnum id) {
		return id;
	}

	@RequestMapping(value = "/pub/amx/dnum", method = RequestMethod.GET)
	public DigitsDnum geoLocation(@RequestParam(required = false) DigitsDnum id) {
		new DigitsDnum("FOUR", 3);
		return id;
	}

	@RequestMapping(value = "/pub/amx/hmac", method = RequestMethod.GET)
	public Map<String, String> hmac(@RequestParam Long interval, @RequestParam String secret,
			@RequestParam String message, @RequestParam Integer length) {
		Map<String, String> map = new HashMap<String, String>();
		HashBuilder builder = new HashBuilder().interval(interval).secret(secret).message(message);
		map.put("hmac", builder.toHMAC().output());
		map.put("numeric", builder.toNumeric(length).output());
		return map;
	}

}
