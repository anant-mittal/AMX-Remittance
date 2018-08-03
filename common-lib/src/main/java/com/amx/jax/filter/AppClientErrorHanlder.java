package com.amx.jax.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.AmxHttpExceptions.AmxHttpClientException;
import com.amx.jax.exception.AmxHttpExceptions.AmxHttpNotFoundException;
import com.amx.jax.exception.AmxHttpExceptions.AmxHttpServerException;
import com.amx.jax.exception.ExceptionFactory;
import com.amx.utils.ArgUtil;
import com.amx.utils.IoUtils;
import com.amx.utils.JsonUtil;

@Component
public class AppClientErrorHanlder implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode() != HttpStatus.OK) {
			return true;
		}
		String apiErrorJson = (String) response.getHeaders().getFirst("apiErrorJson");
		if (!ArgUtil.isEmpty(apiErrorJson)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		HttpStatus statusCode = response.getStatusCode();
		String statusText = response.getStatusText();
		String apiErrorJson = ArgUtil.parseAsString(response.getHeaders().getFirst("apiErrorJson"));
		AmxApiError apiError = throwError(apiErrorJson);

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new AmxHttpNotFoundException(statusCode);
		}

		if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			String body = IoUtils.inputstream_to_string(response.getBody());
			apiError = throwError(body);
			throw new AmxHttpServerException(statusCode, apiError);
		} else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			String body2 = IoUtils.inputstream_to_string(response.getBody());
			apiError = throwError(body2);
			throw new AmxHttpClientException(statusCode, apiError);
		}

	}

	private AmxApiError throwError(String apiErrorJson) {
		AmxApiError apiError = JsonUtil.fromJson(apiErrorJson, AmxApiError.class);
		if (!ArgUtil.isEmpty(apiError)) {
			AmxApiException defExcp = ExceptionFactory.get(apiError.getException());
			if (defExcp == null) {
				defExcp = ExceptionFactory.get(apiError.getErrorId());
			}
			if (defExcp != null) {
				throw defExcp.getInstance(apiError);
			}
		}
		return apiError;
	}

	static {
		ExceptionFactory.readExceptions();
	}
}
