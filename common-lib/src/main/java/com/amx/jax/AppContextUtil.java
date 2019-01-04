package com.amx.jax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.MDC;
import org.springframework.http.HttpHeaders;

import com.amx.jax.dict.Tenant;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.RequestType;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.UniqueID;

public class AppContextUtil {

	/**
	 * 
	 * @param generate - create new token if not present
	 * @param override - create new token anyway
	 * @return -returns current token
	 */
	public static String getTraceId(boolean generate, boolean override) {
		String sessionId = getSessionId();
		if (override) {
			if (ArgUtil.isEmpty(sessionId)) {
				sessionId = UniqueID.generateString();
			}
			return ContextUtil.generateTraceId(sessionId);
		}
		String traceId = ContextUtil.getTraceId(false);
		if (generate && ArgUtil.isEmpty(traceId)) {
			return ContextUtil.getTraceId(true, sessionId);
		}
		return traceId;
	}

	public static String getTraceId(boolean generate) {
		return getTraceId(generate, false);
	}

	public static String getTraceId() {
		return getTraceId(true, false);
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

	public static String getSessionId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.SESSION_ID_XKEY));
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

	public static RequestType getRequestType() {
		return (RequestType) ArgUtil.parseAsEnum(ContextUtil.map().get(AppConstants.REQUEST_TYPE_XKEY),
				RequestType.DEFAULT);
	}

	public static final Pattern pattern = Pattern.compile("^([A-Z]{3})-([\\w]+)-(\\w+)$");

	public static String getSessionIdFromTraceId() {
		String traceId = getTraceId();
		if (ArgUtil.isEmptyString(traceId)) {
			Matcher matcher = pattern.matcher(traceId);
			if (matcher.find()) {
				setSessionId(matcher.group(1) + "-" + matcher.group(2));
			}
		}
		return getSessionId();
	}

	public static Tenant getTenant() {
		return TenantContextHolder.currentSite();
	}

	public static void setTenant(Tenant tenant) {
		TenantContextHolder.setCurrent(tenant);
	}

	public static void setTranceId(String traceId) {
		ContextUtil.setTraceId(traceId);
	}

	public static void setTranxId(String tranxId) {
		ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, tranxId);
	}

	public static void setTraceTime(long timestamp) {
		ContextUtil.map().put(AppConstants.TRACE_TIME_XKEY, timestamp);
	}

	public static void setActorId(Object actorId) {
		ContextUtil.map().put(AppConstants.ACTOR_ID_XKEY, actorId);
	}

	public static void setSessionId(Object sessionId) {
		ContextUtil.map().put(AppConstants.SESSION_ID_XKEY, sessionId);
	}

	public static void setRequestType(RequestType reqType) {
		ContextUtil.map().put(AppConstants.REQUEST_TYPE_XKEY, reqType);
	}

	public static void setUserClient(UserDeviceClient userClient) {
		ContextUtil.map().put(AppConstants.USER_CLIENT_XKEY, userClient);
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
		appContext.setTranxId(getTranxId());
		appContext.setActorId(getActorId());
		appContext.setTraceTime(getTraceTime());
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
		if (context.getActorId() != null) {
			setActorId(context.getActorId());
		}

		if (context.getClient() != null) {
			setUserClient(context.getClient());
		}

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
		map.put(AppConstants.TRANX_ID_XKEY, context.getTranxId());
		map.put(AppConstants.ACTOR_ID_XKEY, context.getActorId());
		map.put(AppConstants.USER_CLIENT_XKEY, JsonUtil.toJson(context.getClient()));
		return map;
	}

	/**
	 * Fills header with required header values
	 * 
	 * @param httpHeaders
	 */
	public static void exportAppContextTo(HttpHeaders httpHeaders) {

		String traceId = getTraceId();
		String tranxId = getTranxId();
		String userId = getActorId();
		UserDeviceClient userClient = getUserClient();
		httpHeaders.add(TenantContextHolder.TENANT, getTenant().toString());
		if (!ArgUtil.isEmpty(traceId)) {
			httpHeaders.add(AppConstants.TRACE_ID_XKEY, traceId);
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
	}

	/**
	 * This method is called when we receive response from other service
	 * 
	 * @param httpHeaders
	 */
	public static void importAppContextFrom(HttpHeaders httpHeaders) {
		if (httpHeaders.containsKey(AppConstants.TRANX_ID_XKEY)) {
			List<String> tranxids = httpHeaders.get(AppConstants.TRANX_ID_XKEY);
			if (tranxids.size() >= 0) {
				setTranxId(tranxids.get(0));
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
