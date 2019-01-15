package com.amx.jax.filter;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;

import com.amx.jax.AppParam;

public class AppRequestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppRequestUtil.class);

	public static LinkedMultiValueMap<String, String> getHeader(HttpServletRequest req) {
		LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = req.getHeaders(headerName);
			while (headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				headerMap.add(headerName, headerValue);
			}
		}
		return headerMap;
	}

	public static void printIfDebug(HttpServletRequest req) {
		if (LOGGER.isDebugEnabled()) {
			LinkedMultiValueMap<String, String> headerMap = getHeader(req);
			LOGGER.debug("*** REQT_OUT_HEADER *****: {}", headerMap.toString());
		}
	}

	private static LinkedMultiValueMap<String, String> getHeader(HttpServletResponse resp) {
		LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		Collection<String> headerNames = resp.getHeaderNames();
		for (String headerName : headerNames) {
			List<String> values = (List<String>) resp.getHeaders(headerName);
			headerMap.put(headerName, values);
		}
		return headerMap;
	}

	public static void printIfDebug(HttpServletResponse resp) {
		if (LOGGER.isDebugEnabled()) {
			LinkedMultiValueMap<String, String> headerMap = getHeader(resp);
			LOGGER.debug("*** REQT_OUT_HEADER *****: {}", headerMap.toString());
		}
	}

}
