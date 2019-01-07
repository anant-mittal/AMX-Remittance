package com.amx.jax.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.AppParam;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;
import com.amx.utils.CryptoUtil;

@Component
public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppClientInterceptor.class);

	@Autowired
	AppConfig appConfig;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		AppContextUtil.exportAppContextTo(request.getHeaders());
		// restMetaService.exportMetaTo(request.getHeaders());

		request.getHeaders().add(AppConstants.AUTH_KEY_XKEY,
				CryptoUtil.generateHMAC(appConfig.getAppAuthKey(), AppContextUtil.getTraceId()));
		RequestTrackEvent requestTrackEvent = new RequestTrackEvent(request);
		AuditServiceClient.trackStatic(requestTrackEvent);

		if (AppParam.PRINT_TRACK_BODY.isEnabled() || LOGGER.isDebugEnabled()) {
			LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
			Collection<Entry<String, List<String>>> headers = request.getHeaders().entrySet();
			for (Entry<String, List<String>> header : headers) {
				headerMap.put(header.getKey(), header.getValue());
			}
			LOGGER.debug("*** REQT_OUT_HEADER *****: {}", headerMap.toString());
			LOGGER.debug("*** REQT_OUT_BODY   *****: {}", new String(body, "UTF-8"));
		}

		ClientHttpResponse response = execution.execute(request, body);
		AppContextUtil.importAppContextFrom(response.getHeaders());
		AuditServiceClient.trackStatic(new RequestTrackEvent(response, request));

		if (AppParam.PRINT_TRACK_BODY.isEnabled() || LOGGER.isDebugEnabled()) {
			return traceResponse(response);
		}

		return response;
	}

	private ClientHttpResponse traceResponse(ClientHttpResponse response) throws IOException {
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
		LOGGER.debug("*** REQT_OUT_HEADER *****: {}", headerMap.toString());
		LOGGER.debug("*** RESP_IN_BODY    *****: {}", inputStringBuilder.toString());
		return responseWrapper;
	}

}
