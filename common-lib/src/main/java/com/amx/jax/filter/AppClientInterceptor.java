package com.amx.jax.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.amx.jax.AppContextUtil;
import com.amx.jax.AppParam;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;

public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppClientInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		AppContextUtil.exportAppContextTo(request.getHeaders());
		AuditServiceClient.trackStatic(new RequestTrackEvent(request));
		
		if (AppParam.PRINT_TRACK_BODY.isEnabled()) {
			LOGGER.info("*** REQUEST_BODY *****: {}", new String(body, "UTF-8"));
		}
		ClientHttpResponse response = execution.execute(request, body);
		AppContextUtil.importAppContextFrom(response.getHeaders());
		AuditServiceClient.trackStatic(new RequestTrackEvent(response, request));

		if (AppParam.PRINT_TRACK_BODY.isEnabled()) {
			return traceResponse(response);
		}

		return response;
	}

	private ClientHttpResponse traceResponse(ClientHttpResponse response) throws IOException {
		final ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);
		StringBuilder inputStringBuilder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseWrapper.getBody(), "UTF-8"));
		String line = bufferedReader.readLine();
		while (line != null) {
			inputStringBuilder.append(line);
			inputStringBuilder.append('\n');
			line = bufferedReader.readLine();
		}
		LOGGER.info("*** RESPONSE_BODY ****: {}", inputStringBuilder.toString());
		return responseWrapper;
	}

}
