package com.amx.jax.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import com.amx.jax.AppUtil;

public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	final static Logger LOGGER = LoggerFactory.getLogger(AppClientInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		Map<String, String> header = AppUtil.header();
		for (Entry<String, String> b : header.entrySet()) {
			if (!StringUtils.isEmpty(b.getValue())) {
				request.getHeaders().add(b.getKey(), b.getValue());
			}
		}

		LOGGER.info("REQT {}={} : {}", request.getMethod(), request.getURI(), request.getHeaders());
		ClientHttpResponse response = execution.execute(request, body);
		LOGGER.info("RESP {}={} : {}", response.getStatusCode(), response.getStatusText(), response.getHeaders());

		return response;
	}

}