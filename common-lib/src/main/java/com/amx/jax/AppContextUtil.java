package com.amx.jax;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.amx.jax.api.AmxFieldError;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.CommonHttpRequest.ApiRequestDetail;
import com.amx.jax.http.RequestType;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.StringUtils;
import com.amx.utils.UniqueID;
import com.fasterxml.jackson.core.type.TypeReference;

public class AppContextUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppContextUtil.class);

	public static void setSessionPrefix(String sessionPrefix) {
		ContextUtil.map().put(AppConstants.SESSION_PREFIX_XKEY, sessionPrefix);
	}

	public static void setRequestUser(String sessionSuffix) {
		ContextUtil.map().put(AppConstants.SESSION_SUFFIX_XKEY, sessionSuffix);
	}

	public static String getRequestUser() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.SESSION_SUFFIX_XKEY),
				Constants.BLANK);
	}

	public static void setSessionId(Object sessionId) {
		ContextUtil.map().put(AppConstants.SESSION_ID_XKEY, sessionId);
	}

	public static String getSessionId(boolean generate, String defautSessionId) {
		String sessionId = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.SESSION_ID_XKEY), defautSessionId);
		String sessionPrefix = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.SESSION_PREFIX_XKEY),
				Constants.BLANK);
		if (generate && ArgUtil.isEmptyString(sessionId)) {
			sessionId = UniqueID.generateSessionId(sessionPrefix);
			setSessionId(sessionId);
		}
		return sessionId;
	}

	public static String getSessionId(boolean generate) {
		return getSessionId(generate, null);
	}

	public static String getSessionId(String defautSessionId) {
		return getSessionId(true, defautSessionId);
	}

	/**
	 * 
	 * @param generate - create new token if absent
	 * @param override - create new token anyway
	 * @return -returns current token
	 */
	public static String getTraceId(boolean generate, boolean override) {
		String sessionId = getSessionId(false);
		if (override) {
			if (ArgUtil.isEmpty(sessionId)) {
				sessionId = getSessionId(true);
			}
			return ContextUtil.generateTraceId(sessionId, getRequestUser());
		}
		String traceId = ContextUtil.getTraceId(false);
		if (generate && ArgUtil.isEmpty(traceId)) {
			return ContextUtil.getTraceId(true, sessionId, getRequestUser());
		}
		return traceId;
	}

	/**
	 * 
	 * @param generate - create new token if absent
	 * @return
	 */
	public static String getTraceId(boolean generate) {
		return getTraceId(generate, false);
	}

	public static String getTraceId() {
		return getTraceId(true, false);
	}

	public static String getContextId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.CONTEXT_ID_XKEY));
	}

	public static String getTranxId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
	}

	public static String getTranxId(boolean generate) {
		String key = getTranxId();
		if (generate && ArgUtil.isEmptyString(key)) {
			key = getTraceId();
			setTranxId(key);
		}
		return key;
	}

	public static Long getTraceTime() {
		return ArgUtil.parseAsLong(ContextUtil.map().get(AppConstants.TRACE_TIME_XKEY), 0L);
	}

	public static String getActorId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.ACTOR_ID_XKEY));
	}

	public static Language getLang() {
		return (Language) ArgUtil.parseAsEnum(ContextUtil.map().get(AppConstants.LANG_PARAM_KEY), Language.EN,
				Language.class);
	}

	public static Language getLang(Language lang) {
		return (Language) ArgUtil.parseAsEnum(ContextUtil.map().get(AppConstants.LANG_PARAM_KEY), lang, Language.class);
	}

	public static UserDeviceClient getUserClient() {
		Object userDeviceClientObject = ContextUtil.map().get(AppConstants.USER_CLIENT_XKEY);
		UserDeviceClient userDeviceClient = null;
		if (userDeviceClientObject == null) {
			userDeviceClient = new UserDeviceClient();
			ContextUtil.map().put(AppConstants.USER_CLIENT_XKEY, userDeviceClient);
		} else {
			userDeviceClient = (UserDeviceClient) userDeviceClientObject;
		}
		return userDeviceClient;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getParams() {
		Object userDeviceClientObject = ContextUtil.map().get(AppConstants.REQUEST_PARAMS_XKEY);
		Map<String, Object> userDeviceClient = null;
		if (userDeviceClientObject == null) {
			userDeviceClient = new HashMap<String, Object>();
			ContextUtil.map().put(AppConstants.REQUEST_PARAMS_XKEY, userDeviceClient);
		} else {
			userDeviceClient = (Map<String, Object>) userDeviceClientObject;
		}
		return userDeviceClient;
	}

	/**
	 * @deprecated use {@link #getApiRequestDetail()}
	 * 
	 * @return
	 */
	@Deprecated
	public static RequestType getRequestType() {
		return (RequestType) ArgUtil.parseAsEnum(ContextUtil.map().get(AppConstants.REQUEST_TYPE_XKEY),
				RequestType.DEFAULT, RequestType.class);
	}

	public static ApiRequestDetail getApiRequestDetail() {
		return (ApiRequestDetail) ContextUtil.map().get(AppConstants.REQUEST_DETAILS_XKEY);
	}

	public static String getSessionIdFromTraceId() {
		String traceId = getTraceId();
		if (!ArgUtil.isEmptyString(traceId)) {
			Matcher matcher = UniqueID.SYSTEM_STRING_PATTERN_V2.matcher(traceId);
			if (matcher.find()) {
				setSessionId(matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3));
			} else {
				Matcher matcher2 = UniqueID.SYSTEM_STRING_PATTERN.matcher(traceId);
				if (matcher2.find()) {
					setSessionId(matcher2.group(1) + "-" + matcher2.group(2) + "-" + matcher2.group(3));
				}
			}
		}
		return getSessionId(true);
	}

	public static Tenant getTenant() {
		return TenantContextHolder.currentSite();
	}

	public static void setTenant(Tenant tenant) {
		TenantContextHolder.setCurrent(tenant);
	}

	public static void setVendor(Class<?> class1, String vendor) {
		ContextUtil.map().put("scopedTarget." + StringUtils.decapitalize(class1.getSimpleName()), vendor);
	}

	public static String getVendor(Class<?> class1) {
		return (String) ContextUtil.map().get("scopedTarget." + StringUtils.decapitalize(class1.getSimpleName()));
	}

	public static String getVendor(String scopedTargetClassName) {
		return (String) ContextUtil.map().get(scopedTargetClassName);
	}

	public static void setTranceId(String traceId) {
		ContextUtil.setTraceId(traceId);
	}

	public static void setContextId(String contextId) {
		ContextUtil.map().put(AppConstants.CONTEXT_ID_XKEY, contextId);
	}

	public static void setTranxId(String tranxId) {
		ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, tranxId);
	}

	public static void setFlowfix(String flowfix) {
		ContextUtil.setFlowfix(flowfix);
	}

	public static void setFlow(String flow) {
		getParams().put("flow", flow);
	}

	public static String getFlow() {
		return ArgUtil.parseAsString(getParams().get("flow"));
	}

	public static void setTraceTime(long timestamp) {
		ContextUtil.map().put(AppConstants.TRACE_TIME_XKEY, timestamp);
	}

	public static void resetTraceTime() {
		ContextUtil.map().put(AppConstants.TRACE_TIME_XKEY, System.currentTimeMillis());
	}

	public static void setActorId(Object actorId) {
		ContextUtil.map().put(AppConstants.ACTOR_ID_XKEY, actorId);
	}

	public static void setLang(Object lang) {
		ContextUtil.map().put(AppConstants.LANG_PARAM_KEY, lang);
	}

	public static void setRequestType(RequestType reqType) {
		ContextUtil.map().put(AppConstants.REQUEST_TYPE_XKEY, reqType);
	}

	public static void setApiRequestDetail(ApiRequestDetail apiRequestDetail) {
		ContextUtil.map().put(AppConstants.REQUEST_DETAILS_XKEY, apiRequestDetail);
	}

	public static void setUserClient(UserDeviceClient userClient) {
		ContextUtil.map().put(AppConstants.USER_CLIENT_XKEY, userClient);
	}

	@SuppressWarnings("unchecked")
	public static List<AmxFieldError> getWarnings() {
		Object userDeviceClientObject = ContextUtil.map().get(AppConstants.REQUEST_WARNING_XKEY);
		List<AmxFieldError> warnings = null;
		if (userDeviceClientObject == null) {
			warnings = new ArrayList<AmxFieldError>();
			ContextUtil.map().put(AppConstants.REQUEST_WARNING_XKEY, warnings);
		} else {
			warnings = (List<AmxFieldError>) userDeviceClientObject;
		}
		return warnings;
	}

	public static void addWarning(AmxFieldError warning) {
		getWarnings().add(warning);
	}

	public static void addWarning(String warning) {
		AmxFieldError w = new AmxFieldError();
		w.setDescription(warning);
		addWarning(w);
	}

	public static void setParams(String requestParamsJson, String requestdParamsJson) {
		try {
			if (!ArgUtil.isEmpty(requestdParamsJson)) {
				byte[] decodedBytes = Base64.getDecoder().decode(requestdParamsJson);
				requestParamsJson = new String(decodedBytes);
			}
			if (!ArgUtil.isEmpty(requestParamsJson)) {
				ContextUtil.map().put(AppConstants.REQUEST_PARAMS_XKEY,
						JsonUtil.fromJson(requestParamsJson, new TypeReference<Map<String, Object>>() {
						}));
			}
		} catch (Exception e) {
			// Fail Silenty
			LOGGER.error("***xxxxx***** NOT ABLE TO UNDERSTAND REQUEST PARAMS  ***xxxxx***** ");
		}
	}

	public static <T> void set(String contextKey, T value) {
		ContextUtil.map().put(contextKey, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String contextKey) {
		return (T) ContextUtil.map().get(contextKey);
	}

	public static void init() {
		MDC.put(ContextUtil.TRACE_ID, getTraceId());
		MDC.put(TenantContextHolder.TENANT, getTenant());
	}

	public static void clear() {
		MDC.clear();
		ContextUtil.clear();
	}

	public static AppContext getContext() {
		AppContext appContext = new AppContext();
		appContext.setTenant(getTenant());
		appContext.setTraceId(getTraceId());
		appContext.setContextId(getContextId());
		appContext.setTranxId(getTranxId());
		appContext.setActorId(getActorId());
		appContext.setTraceTime(getTraceTime());
		appContext.setClient(getUserClient());
		appContext.setParams(getParams());
		appContext.apiRequestDetail(getApiRequestDetail());
		return appContext;
	}

	public static AppContext setContext(AppContext context) {
		if (context.getTraceId() != null) {
			ContextUtil.setTraceId(context.getTraceId());
		}
		if (context.getTenant() != null) {
			TenantContextHolder.setCurrent(context.getTenant());
		}
		if (context.getTranxId() != null) {
			setTranxId(context.getTranxId());
		}
		if (context.getContextId() != null) {
			setContextId(context.getContextId());
		}
		if (context.getActorId() != null) {
			setActorId(context.getActorId());
		}

		if (context.getClient() != null) {
			setUserClient(context.getClient());
		}

		setApiRequestDetail(context.apiRequestDetail());

		setTraceTime(context.getTraceTime());

		return context;
	}

	public static Map<String, String> header() {
		return header(new HashMap<String, String>());
	}

	private static Map<String, String> header(Map<String, String> map) {
		AppContext context = getContext();
		map.put(TenantContextHolder.TENANT, context.getTenant().toString());
		map.put(AppConstants.TRACE_ID_XKEY, context.getTraceId());
		map.put(AppConstants.CONTEXT_ID_XKEY, context.getContextId());
		map.put(AppConstants.TRANX_ID_XKEY, context.getTranxId());
		map.put(AppConstants.ACTOR_ID_XKEY, context.getActorId());
		map.put(AppConstants.USER_CLIENT_XKEY, JsonUtil.toJson(context.getClient()));
		map.put(AppConstants.REQUEST_PARAMS_XKEY, JsonUtil.toJson(context.getParams()));
		return map;
	}

	/**
	 * Fills header with required header values
	 * 
	 * @param httpHeaders
	 */
	public static void exportAppContextTo(HttpHeaders httpHeaders) {

		String sessionId = getSessionId(true);
		String traceId = getTraceId();
		String contextId = getContextId();
		String tranxId = getTranxId();
		String userId = getActorId();
		Language lang = getLang();
		UserDeviceClient userClient = getUserClient();
		Map<String, Object> params = getParams();
		httpHeaders.add(TenantContextHolder.TENANT, getTenant().toString());

		if (!ArgUtil.isEmpty(sessionId)) {
			httpHeaders.add(AppConstants.SESSION_ID_XKEY, sessionId);
		}
		if (!ArgUtil.isEmpty(traceId)) {
			httpHeaders.add(AppConstants.TRACE_ID_XKEY, traceId);
		}
		if (!ArgUtil.isEmpty(traceId)) {
			httpHeaders.add(AppConstants.LANG_PARAM_KEY, lang.toString());
		}
		if (!ArgUtil.isEmpty(contextId)) {
			httpHeaders.add(AppConstants.CONTEXT_ID_XKEY, contextId);
		}
		if (!ArgUtil.isEmpty(tranxId)) {
			httpHeaders.add(AppConstants.TRANX_ID_XKEY, tranxId);
		}
		if (!ArgUtil.isEmpty(userId)) {
			httpHeaders.add(AppConstants.ACTOR_ID_XKEY, userId);
		}
		if (!ArgUtil.isEmpty(userClient)) {
			httpHeaders.add(AppConstants.USER_CLIENT_XKEY, JsonUtil.toJson(userClient));
		}
		if (!ArgUtil.isEmpty(params)) {
			httpHeaders.add(AppConstants.REQUEST_PARAMS_XKEY, JsonUtil.toJson(params));
		}
	}

	/**
	 * This method is called when we receive response from other service
	 * 
	 * @param httpHeaders
	 */
	public static void importAppContextFromResponseHEader(HttpHeaders httpHeaders) {
		if (httpHeaders.containsKey(AppConstants.TRANX_ID_XKEY)) {
			List<String> tranxids = httpHeaders.get(AppConstants.TRANX_ID_XKEY);
			if (tranxids.size() >= 0) {
				setTranxId(tranxids.get(0));
			}
		}

		if (httpHeaders.containsKey(AppConstants.CONTEXT_ID_XKEY)) {
			List<String> cntxtxids = httpHeaders.get(AppConstants.CONTEXT_ID_XKEY);
			if (cntxtxids.size() >= 0) {
				setContextId(cntxtxids.get(0));
			}
		}

		String traceId = getTraceId(false);
		if (ArgUtil.isEmpty(traceId)) {
			if (httpHeaders.containsKey(AppConstants.TRACE_ID_XKEY)) {
				List<String> traceIds = httpHeaders.get(AppConstants.TRACE_ID_XKEY);
				if (traceIds.size() >= 0) {
					setTranceId(traceIds.get(0));
				}
			}
		}

	}

}
