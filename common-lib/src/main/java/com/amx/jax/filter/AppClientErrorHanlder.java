package com.amx.jax.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.ExceptionFactory;
import com.amx.utils.ArgUtil;
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

		String apiErrorJson = (String) response.getHeaders().getFirst("apiErrorJson");

		AmxApiError apiError = JsonUtil.fromJson(apiErrorJson, AmxApiError.class);

		if (!ArgUtil.isEmpty(apiError)) {
			AmxApiException defExcp = ExceptionFactory.get(apiError.getErrorClass());

			if (defExcp == null) {
				defExcp = ExceptionFactory.get(apiError.getErrorId());
			}

			if (defExcp != null) {
				throw defExcp.getInstance(apiError);
			}
		} else if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			// handle SERVER_ERROR
		} else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			// handle CLIENT_ERROR
			if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
				// throw new NotFoundException();
			}
		}

	}

	static {
		ExceptionFactory.readExceptions();
	}
}
