package com.amx.jax.filter;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;

public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		AppContextUtil.importHeadersTo(request.getHeaders());
		AuditServiceClient.trackStatic(new RequestTrackEvent(request));
		ClientHttpResponse response = execution.execute(request, body);
		AuditServiceClient.trackStatic(new RequestTrackEvent(response, request));
		AppContextUtil.exportHeadersFrom(response.getHeaders());
		return response;
	}

}
