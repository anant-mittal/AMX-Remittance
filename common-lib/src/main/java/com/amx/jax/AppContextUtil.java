package com.amx.jax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static String getUserId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.USER_ID_XKEY));
	}

	public static String getSessionId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.SESSION_ID_XKEY));
	}

	public static Tenant getTenant() {
		return TenantContextHolder.currentSite();
	}

	public static void setTranxId(String tranxId) {
		ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, tranxId);
	}

	public static void setUserId(Object userId) {
		ContextUtil.map().put(AppConstants.USER_ID_XKEY, userId);
	}

	public static void setSessionId(Object sessionId) {
		ContextUtil.map().put(AppConstants.SESSION_ID_XKEY, sessionId);
	}

	public static AppContext getContext() {
		AppContext appContext = new AppContext();
		appContext.setTenant(getTenant());
		appContext.setTraceId(getTraceId());
		appContext.setTranxId(getTranxId());
		appContext.setUserId(getUserId());
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
		if (context.getUserId() != null) {
			setUserId(context.getUserId());
		}
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
		map.put(AppConstants.USER_ID_XKEY, context.getUserId());
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
		String userId = getUserId();
		httpHeaders.add(TenantContextHolder.TENANT, getTenant().toString());
		if (!ArgUtil.isEmpty(traceId)) {
			httpHeaders.add(AppConstants.TRACE_ID_XKEY, traceId);
		}
		if (!ArgUtil.isEmpty(tranxId)) {
			httpHeaders.add(AppConstants.TRANX_ID_XKEY, tranxId);
		}
		if (!ArgUtil.isEmpty(userId)) {
			httpHeaders.add(AppConstants.USER_ID_XKEY, userId);
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
