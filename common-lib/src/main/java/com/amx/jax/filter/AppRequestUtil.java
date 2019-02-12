package com.amx.jax.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;

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

	public static HttpServletRequest printIfDebug(HttpServletRequest req) {
		if (LOGGER.isDebugEnabled()) {
			LinkedMultiValueMap<String, String> headerMap = getHeader(req);
			LOGGER.debug(">>>>> RQT-IN-HEDR =====: {}", headerMap.toString());
		}
		return req;
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

	public static HttpServletResponse printIfDebug(HttpServletResponse resp) {
		if (LOGGER.isDebugEnabled()) {
			LinkedMultiValueMap<String, String> headerMap = getHeader(resp);
			LOGGER.debug("<<<<< RSP-OUT-HEDR =====: {}", headerMap.toString());
		}
		return resp;
	}

	public static ClientHttpResponse printIfDebug(ClientHttpResponse response) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			final ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);
			StringBuilder inputStringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(
						new InputStreamReader(responseWrapper.getBody(), "UTF-8"));
				String line = bufferedReader.readLine();
				while (line != null) {
					inputStringBuilder.append(line);
					inputStringBuilder.append('\n');
					line = bufferedReader.readLine();
				}
			} catch (Exception e) {
				LOGGER.error("traceResponse", e);
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}

			// this.header = response.getHeaders();
			LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
			Collection<Entry<String, List<String>>> headers = response.getHeaders().entrySet();
			for (Entry<String, List<String>> header : headers) {
				headerMap.put(header.getKey(), header.getValue());
			}
			LOGGER.debug("===== RSP-IN-HEDR <<<<<: {}", headerMap.toString());
			LOGGER.debug("===== RSP-IN-BODY <<<<<: {}", inputStringBuilder.toString());
			return responseWrapper;
		}
		return response;
	}

	public static void printIfDebug(HttpRequest request, byte[] body) throws UnsupportedEncodingException {
		LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		Collection<Entry<String, List<String>>> headers = request.getHeaders().entrySet();
		for (Entry<String, List<String>> header : headers) {
			headerMap.put(header.getKey(), header.getValue());
		}
		LOGGER.debug("===== RQT-OUT-HEDR >>>>>: {}", headerMap.toString());
		LOGGER.debug("===== RQT-OUT-BODY >>>>>: {}", new String(body, "UTF-8"));
	}

}
