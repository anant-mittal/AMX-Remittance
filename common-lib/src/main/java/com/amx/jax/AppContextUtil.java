package com.amx.jax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public class AppContextUtil {
	public static Map<String, String> header() {
		return header(new HashMap<String, String>());
	}

	public static Map<String, String> header(Map<String, String> map) {
		map.put(TenantContextHolder.TENANT, TenantContextHolder.currentSite().toString());
		map.put(AppConstants.TRACE_ID_XKEY, ContextUtil.getTraceId());
		map.put(AppConstants.TRANX_ID_XKEY, ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY)));
		return map;
	}

	public static void readHeader(HttpHeaders headers) {
		if (headers.containsKey(AppConstants.TRANX_ID_XKEY)) {
			List<String> tranxids = headers.get(AppConstants.TRANX_ID_XKEY);
			if (tranxids.size() >= 0) {
				ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, tranxids.get(0));
			}
		}
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

}
