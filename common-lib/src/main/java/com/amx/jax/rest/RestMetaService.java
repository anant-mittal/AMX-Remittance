package com.amx.jax.rest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;

@Component
public class RestMetaService {

	@Autowired(required = false)
	RestMetaFilter restMetaFilter;

	public static interface RequestRestMeta {
		public void put(String metaKey, String metaValue);
	}

	public static interface ResponseRestMeta {
		public String get(String metaKey);
	}

	public static class RestMeta implements RequestRestMeta, ResponseRestMeta {

		HttpHeaders httpHeaders;
		Map<String, String> map;

		public RestMeta(HttpHeaders httpHeaders) {
			this.httpHeaders = httpHeaders;
		}

		public RestMeta(Map<String, String> map) {
			this.map = map;
		}

		public void put(String metaKey, String metaValue) {
			httpHeaders.add(AppConstants.META_XKEY, String.format("%s=%s", metaKey, metaValue));
		}

		public String get(String metaKey) {
			return map.get(metaKey);
		}
	}

	public void exportMetaTo(HttpHeaders httpHeaders) {
		if (restMetaFilter != null) {
			RequestRestMeta meta = new RestMeta(httpHeaders);
			restMetaFilter.exportMeta(meta);
		}
	}

	public void importMetaFrom(HttpServletRequest req) {
		if (restMetaFilter != null) {
			Map<String, String> map = new HashMap<String, String>();
			Enumeration<String> metaValues = req.getHeaders(AppConstants.META_XKEY);
			while (metaValues.hasMoreElements()) {
				String headerValue = metaValues.nextElement();
				String[] vals = headerValue.split("=");
				if (vals.length == 2) {
					map.put(vals[0], vals[1]);
				}
			}
			ResponseRestMeta meta = new RestMeta(map);
			restMetaFilter.importMeta(meta);
		}
	}
}
