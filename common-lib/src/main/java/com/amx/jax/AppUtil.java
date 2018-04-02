package com.amx.jax;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public class AppUtil {
	public static Map<String, String> header() {
		return header(new HashMap<String, String>());
	}

	public static Map<String, String> header(Map<String, String> map) {
		map.put(TenantContextHolder.TENANT, TenantContextHolder.currentSite().toString());
		map.put(AppConstants.TRACE_ID_XKEY, ContextUtil.getTraceId());
		map.put(AppConstants.TRANX_ID_XKEY, ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY)));
		return map;
	}

}
