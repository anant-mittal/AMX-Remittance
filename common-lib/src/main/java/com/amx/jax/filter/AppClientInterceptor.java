package com.amx.jax.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.amx.jax.AppConstants;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	final static Logger LOGGER = LoggerFactory.getLogger(AppClientInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		request.getHeaders().add(TenantContextHolder.TENANT, TenantContextHolder.currentSite().toString());
		request.getHeaders().add(AppConstants.TRACE_ID_XKEY, ContextUtil.getTraceId());
		request.getHeaders().add(AppConstants.TRANX_ID_XKEY,
				ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY)));

		LOGGER.info("REQT {}={} : {}", request.getMethod(), request.getURI(), request.getHeaders());
		ClientHttpResponse response = execution.execute(request, body);
		LOGGER.info("RESP {}={} : {}", response.getStatusCode(), response.getStatusText(), response.getHeaders());

		return response;
	}

}