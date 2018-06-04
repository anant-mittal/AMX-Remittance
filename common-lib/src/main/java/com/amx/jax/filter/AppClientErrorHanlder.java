package com.amx.jax.filter;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.amx.jax.exception.AbstractAppException;
import com.amx.jax.exception.ApiError;
import com.amx.jax.exception.ExceptionFactory;
import com.amx.utils.JsonUtil;

@Component
public class AppClientErrorHanlder implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode() != HttpStatus.OK) {
			return true;
		}
		String apiErrorJson = (String) response.getHeaders().getFirst("apiErrorJson");
		if (StringUtils.isNotBlank(apiErrorJson)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		String apiErrorJson = (String) response.getHeaders().getFirst("apiErrorJson");
		ApiError apiError = JsonUtil.fromJson(apiErrorJson, ApiError.class);

		AbstractAppException defExcp = ExceptionFactory.get(apiError.getErrorId());

		if (defExcp != null) {
			throw defExcp.getInstance(apiError);
		}

	}
}
