package com.amx.jax.filter;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppConfig;
import com.amx.jax.AppParam;
import com.amx.jax.AppTenantConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.model.UserDevice;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil.HashBuilder;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
public class AppParamController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppParamController.class);
	public static final String PUB_AMX_PREFIX = "/pub/amx";
	public static final String PUBG_AMX_PREFIX = "/pubg/";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	AppConfig appConfig;

	@Autowired
	AppTenantConfig appTenantConfig;

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
		map.put("getAppSpecifcDecryptedProp", appConfig.getAppSpecifcDecryptedProp());
		map.put("getTenantSpecifcDecryptedProp2", appTenantConfig.getTenantSpecifcDecryptedProp2());
		map.put("getTenantSpecifcDecryptedProp", appTenantConfig.getTenantSpecifcDecryptedProp());
		map.put("defaultTenant", appConfig.getDefaultTenant());
		map.put(TenantContextHolder.TENANT, TenantContextHolder.currentSite(false));
		AmxApiResponse<UserDevice, Map<String, Object>> resp = new AmxApiResponse<UserDevice, Map<String, Object>>();
		resp.setMeta(map);
		resp.setData(commonHttpRequest.getUserDevice());
		return resp;
	}

	@RequestMapping(value = "/pub/amx/hmac", method = RequestMethod.GET)
	public Map<String, String> hmac(@RequestParam Long interval, @RequestParam String secret,
			@RequestParam String message, @RequestParam Integer length,
			@RequestParam(required = false) Long currentTime) {
		Map<String, String> map = new HashMap<String, String>();
		HashBuilder builder = new HashBuilder().interval(interval).secret(secret).message(message);
		if (!ArgUtil.isEmpty(currentTime)) {
			builder.currentTime(currentTime);
		}
		map.put("hmac", builder.toHMAC().output());
		map.put("numeric", builder.toNumeric(length).output());
		map.put("complex", builder.toComplex(length).output());
		return map;
	}

	@RequestMapping(value = "/pub/amx/encrypt", method = RequestMethod.GET)
	public Map<String, String> encrypt(@RequestParam String secret,
			@RequestParam String message) {
		Map<String, String> map = new HashMap<String, String>();
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPasswordCharArray(secret.toCharArray());
		map.put("decrypted", message);
		map.put("encrypted", textEncryptor.encrypt(message));
		return map;
	}

	@RequestMapping(value = "/pub/amx/decrypt", method = RequestMethod.GET)
	public Map<String, String> decrypt(@RequestParam String secret,
			@RequestParam String message) {
		Map<String, String> map = new HashMap<String, String>();
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPasswordCharArray(secret.toCharArray());
		map.put("encrypted", message);
		map.put("decrypted", textEncryptor.decrypt(message));
		return map;
	}

	@RequestMapping(value = "/pub/amx/json/decode/b64", method = RequestMethod.POST)
	public Map<String, Object> jsonDecodeB64(@RequestParam String jsond) {
		byte[] decodedBytes = Base64.getDecoder().decode(jsond);
		String requestParamsJson = new String(decodedBytes);
		if (!ArgUtil.isEmpty(requestParamsJson)) {
			return JsonUtil.fromJson(requestParamsJson, new TypeReference<Map<String, Object>>() {
			});
		}
		return null;
	}

	@RequestMapping(value = "/pub/amx/json/encode/b64", method = RequestMethod.POST)
	public String jsonEncodeB64(@RequestBody Map<String, Object> json) {
		String callbackUrl = JsonUtil.toJson(json);
		return Base64.getEncoder().encodeToString(callbackUrl.getBytes());
	}

}
