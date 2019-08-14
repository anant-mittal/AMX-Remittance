package com.amx.jax.filter;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.AppParam;
import com.amx.jax.AppTenantConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.def.IndicatorListner;
import com.amx.jax.def.IndicatorListner.GaugeIndicator;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.model.UserDevice;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantProperties;
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
	public static final String METRIC_URL = PUB_AMX_PREFIX + "/metric";

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	AppConfig appConfig;

	@Autowired
	AppTenantConfig appTenantConfig;

	@Autowired(required = false)
	List<IndicatorListner> listners;

	@ApiRequest(type = RequestType.NO_TRACK_PING)
	@RequestMapping(value = PARAM_URL, method = RequestMethod.GET)
	public AppParam[] geoLocation(@RequestParam(required = false) AppParam id) {
		if (id != null) {
			id.setEnabled(!id.isEnabled());
			LOGGER.info("App Param {} changed to {}", id, id.isEnabled());
		}
		return AppParam.values();
	}

	@ApiRequest(type = RequestType.NO_TRACK_PING)
	@RequestMapping(value = METRIC_URL, method = RequestMethod.GET)
	public Map<String, Object> metric() {
		Map<String, Object> map = new HashMap<String, Object>();
		for (AppParam eachAppParam : AppParam.values()) {
			map.put(eachAppParam.toString(), eachAppParam);
		}
		GaugeIndicator gaugeIndicator = new GaugeIndicator();
		if (!ArgUtil.isEmpty(listners)) {
			for (IndicatorListner eachListner : listners) {
				map.putAll(eachListner.getIndicators(gaugeIndicator));
			}
		}
		return map;
	}

	@Autowired
	TenantProperties tenantProperties;

	/** The env. */
	@Autowired
	private Environment env;

	public String prop(String key) {
		String value = tenantProperties.getProperties().getProperty(key);
		if (ArgUtil.isEmpty(value)) {
			value = env.getProperty(key);
		}
		return ArgUtil.parseAsString(value);
	}

	@RequestMapping(value = "/pub/amx/device", method = RequestMethod.GET)
	public AmxApiResponse<UserDevice, Map<String, Object>> userDevice(@RequestParam(required = false) String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("getAppSpecifcDecryptedProp", appConfig.getAppSpecifcDecryptedProp());
		map.put("getTenantSpecifcDecryptedProp2", appTenantConfig.getTenantSpecifcDecryptedProp2());
		map.put("getTenantSpecifcDecryptedProp", appTenantConfig.getTenantSpecifcDecryptedProp());
		map.put("defaultTenant", appConfig.getDefaultTenant());
		map.put(TenantContextHolder.TENANT, TenantContextHolder.currentSite(false));

		if (!ArgUtil.isEmpty(key)) {
			map.put(key, prop(key));
		}

		AppContextUtil.addWarning("THis is a warning for no reason");
		AmxApiResponse<UserDevice, Map<String, Object>> resp = new AmxApiResponse<UserDevice, Map<String, Object>>();
		resp.setMeta(map);
		resp.setData(commonHttpRequest.getUserDevice().toSanitized());
		return resp;
	}

	@RequestMapping(value = "/pub/amx/hmac", method = RequestMethod.GET)
	public Map<String, String> hmac(@RequestParam Long interval, @RequestParam String secret,
			@RequestParam String message, @RequestParam Integer length,
			@RequestParam(required = false) Long currentTime, @RequestParam(required = false) String complexHash) {
		Map<String, String> map = new HashMap<String, String>();
		HashBuilder builder = new HashBuilder().interval(interval).secret(secret).message(message);
		if (!ArgUtil.isEmpty(currentTime)) {
			builder.currentTime(currentTime);
		}
		map.put("hmac", builder.toHMAC().output());
		map.put("numeric", builder.toNumeric(length).output());
		map.put("complex", builder.toComplex(length).output());
		if (!ArgUtil.isEmpty(complexHash)) {
			map.put("valid", "" + builder.validateComplexHMAC(complexHash));
		}

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

	@RequestMapping(value = "/pub/error/{exception}/{statusKey}", method = RequestMethod.GET)
	public AmxApiError jsonEncodeB64(@RequestParam String status, @RequestParam String exception) {
		AmxApiError error = new AmxApiError(status, status);
		error.setException(exception);
		return error;
	}
}
