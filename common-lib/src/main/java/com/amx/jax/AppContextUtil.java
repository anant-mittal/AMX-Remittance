package com.amx.jax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public class AppContextUtil {

	public static AppContext getContext() {
		String tranxId = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		AppContext appContext = new AppContext();
		appContext.setTenant(TenantContextHolder.currentSite());
		appContext.setTraceId(ContextUtil.getTraceId());
		appContext.setTranxId(tranxId);
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
			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, context.getTranxId());
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
		return map;
	}

	public static String getTraceId() {
		return ContextUtil.getTraceId();
	}

	public static String getTranxId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
	}

	public static String getTenant() {
		return TenantContextHolder.currentSite().toString();
	}

	/**
	 * Fills header with required header values
	 * 
	 * @param httpHeaders
	 */
	public static void importHeadersTo(HttpHeaders httpHeaders) {

		String traceId = ContextUtil.getTraceId();
		String tranxId = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		httpHeaders.add(TenantContextHolder.TENANT, TenantContextHolder.currentSite().toString());
		if (!ArgUtil.isEmpty(traceId)) {
			httpHeaders.add(AppConstants.TRACE_ID_XKEY, traceId);
		}
		if (!ArgUtil.isEmpty(tranxId)) {
			httpHeaders.add(AppConstants.TRANX_ID_XKEY, tranxId);
		}
	}

	public static void exportHeadersFrom(HttpHeaders httpHeaders) {
		if (httpHeaders.containsKey(AppConstants.TRANX_ID_XKEY)) {
			List<String> tranxids = httpHeaders.get(AppConstants.TRANX_ID_XKEY);
			if (tranxids.size() >= 0) {
				ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, tranxids.get(0));
			}
		}
	}

}
