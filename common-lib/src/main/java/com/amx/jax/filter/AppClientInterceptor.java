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
import com.amx.utils.CryptoUtil;

@Component
public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	@Autowired
	AppConfig appConfig;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		AppContextUtil.exportAppContextTo(request.getHeaders());
		// restMetaService.exportMetaTo(request.getHeaders());

		request.getHeaders().add(AppConstants.AUTH_TOKEN_XKEY,
				CryptoUtil.generateHMAC(appConfig.getAppAuthKey(), AppContextUtil.getTraceId()));
		RequestTrackEvent requestTrackEvent = new RequestTrackEvent(request);
		AuditServiceClient.trackStatic(requestTrackEvent);

		AppRequestUtil.printIfDebug(request, body);

		ClientHttpResponse response = execution.execute(request, body);
		AppContextUtil.importAppContextFrom(response.getHeaders());
		AuditServiceClient.trackStatic(new RequestTrackEvent(response, request));

		return AppRequestUtil.printIfDebug(response);
	}

}
