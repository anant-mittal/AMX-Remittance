package com.amx.jax.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.amx.jax.AppConstants;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.ApiHttpExceptions.ApiErrorException;
import com.amx.jax.exception.ApiHttpExceptions.ApiHttpClientException;
import com.amx.jax.exception.ApiHttpExceptions.ApiHttpNotFoundException;
import com.amx.jax.exception.ApiHttpExceptions.ApiHttpServerException;
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
		String apiErrorJson = (String) response.getHeaders().getFirst(AppConstants.ERROR_HEADER_KEY);
		if (!ArgUtil.isEmpty(apiErrorJson)) {
			return true;
		}
		Object hasExceptionHeader = response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_KEY);
		if (!ArgUtil.isEmpty(hasExceptionHeader)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		HttpStatus statusCode = response.getStatusCode();
		String statusText = response.getStatusText();
		String apiErrorJson = ArgUtil.parseAsString(response.getHeaders().getFirst(AppConstants.ERROR_HEADER_KEY));
		AmxApiError apiError = throwError(apiErrorJson);

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new ApiHttpNotFoundException(statusCode);
		}

		boolean hasExceptionHeader = !ArgUtil
				.isEmpty(response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_KEY));

		if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			String body = IoUtils.inputstream_to_string(response.getBody());
			apiError = throwError(body);
			throw new ApiHttpServerException(statusCode, apiError);
		} else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			String body2 = IoUtils.inputstream_to_string(response.getBody());
			apiError = throwError(body2);
			throw new ApiHttpClientException(statusCode, apiError);
		} else if (hasExceptionHeader) {
			String body = IoUtils.inputstream_to_string(response.getBody());
			apiError = throwError(body);
			throw new ApiErrorException(apiError);
		}

	}

	private AmxApiError throwError(String apiErrorJson) {
		AmxApiError apiError = JsonUtil.fromJson(apiErrorJson, AmxApiError.class);
		if (!ArgUtil.isEmpty(apiError)) {
			AmxApiException defExcp = ExceptionFactory.get(apiError.getException());
			if (defExcp == null) {
				defExcp = ExceptionFactory.get(apiError.getErrorKey());
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
