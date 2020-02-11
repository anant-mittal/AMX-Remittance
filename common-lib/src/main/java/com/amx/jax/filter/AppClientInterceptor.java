package com.amx.jax.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;
import com.amx.jax.rest.AppRequestContextOutFilter;
import com.amx.utils.CryptoUtil;

@Component
public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	@Autowired
	AppConfig appConfig;

	@Autowired(required = false)
	AppRequestContextOutFilter appContextOutFilter;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		if (appContextOutFilter != null) {
			appContextOutFilter.appRequestContextOutFilter(request);
		}

		AppContextUtil.exportAppContextTo(request.getHeaders());
		// restMetaService.exportMetaTo(request.getHeaders());

		request.getHeaders().add(AppConstants.AUTH_TOKEN_XKEY,
				CryptoUtil.generateHMAC(appConfig.getAppAuthKey(), AppContextUtil.getTraceId()));
		RequestTrackEvent requestTrackEvent = new RequestTrackEvent(request);
		AuditServiceClient.trackStatic(requestTrackEvent);

		AppRequestUtil.printIfDebug(request, body);
		long startTime = System.currentTimeMillis();
		ClientHttpResponse response = execution.execute(request, body);
		AppContextUtil.importAppContextFromResponseHEader(response.getHeaders());
		RequestTrackEvent e = new RequestTrackEvent(response, request);
		e.setResponseTime(System.currentTimeMillis() - startTime);
		AuditServiceClient.trackStatic(e);

		return AppRequestUtil.printIfDebug(response);
	}

}
