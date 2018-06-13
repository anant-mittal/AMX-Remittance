package com.amx.jax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public class AppContextUtil {

	public static String getTraceId() {
		return ContextUtil.getTraceId();
	}

	public static String getTranxId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
	}

	public static Long getTraceTime() {
		return ArgUtil.parseAsLong(ContextUtil.map().get(AppConstants.TRACE_TIME_XKEY));
	}

	public static String getActorId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.ACTOR_ID_XKEY));
	}

	public static String getSessionId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.SESSION_ID_XKEY));
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
		return map;
	}

	/**
	 * Fills header with required header values
	 * 
	 * @param httpHeaders
	 */
	public static void importHeadersTo(HttpHeaders httpHeaders) {

		String traceId = getTraceId();
		String tranxId = getTranxId();
		String userId = getActorId();
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
	}

	public static void exportHeadersFrom(HttpHeaders httpHeaders) {
		if (httpHeaders.containsKey(AppConstants.TRANX_ID_XKEY)) {
			List<String> tranxids = httpHeaders.get(AppConstants.TRANX_ID_XKEY);
			if (tranxids.size() >= 0) {
				setTranxId(tranxids.get(0));
			}
		}
	}

}
