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

import com.amx.jax.AppContextUtil;

public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	final static Logger LOGGER = LoggerFactory.getLogger(AppClientInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		Map<String, String> header = AppContextUtil.header();
		for (Entry<String, String> b : header.entrySet()) {
			if (!StringUtils.isEmpty(b.getValue())) {
				request.getHeaders().add(b.getKey(), b.getValue());
			}
		}

		LOGGER.info("REQT-OUT {}={} : {}", request.getMethod(), request.getURI(), request.getHeaders());
		ClientHttpResponse response = execution.execute(request, body);
		LOGGER.info("RESP-IN {}={} : {}", response.getStatusCode(), request.getURI(), response.getHeaders());

		AppContextUtil.readHeader(response.getHeaders());

		return response;
	}

}
