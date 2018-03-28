package com.amx.jax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
public class AppService {
	public Map<String, String> header() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(TenantContextHolder.TENANT, TenantContextHolder.currentSite().toString());
		headers.put(AppConstants.TRACE_ID_XKEY, ContextUtil.getTraceId());
		headers.put(AppConstants.TRANX_ID_XKEY,
				ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY)));
		return headers;
	}
}
