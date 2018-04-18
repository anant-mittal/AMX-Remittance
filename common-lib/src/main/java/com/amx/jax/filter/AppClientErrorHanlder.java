package com.amx.jax.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.amx.jax.exception.AbstractAppException;
import com.amx.jax.exception.ApiError;
import com.amx.jax.exception.ExceptionFactory;

@Component
public class AppClientErrorHanlder implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode() != HttpStatus.OK) {
			return true;
		}
		List<String> errorCode = response.getHeaders().get("ERROR_CODE");
		if (errorCode != null && !errorCode.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		List<String> errorCodes = response.getHeaders().get("ERROR_CODE");
		List<String> errorMessages = response.getHeaders().get("ERROR_MESSAGE");
		ApiError apiError = new ApiError();
		if (errorCodes != null && !errorCodes.isEmpty()) {
			apiError.setErrorId(errorCodes.get(0));
		}
		if (errorMessages != null && !errorMessages.isEmpty()) {
			apiError.setErrorMessage(errorMessages.get(0));
		}

		AbstractAppException defExcp = ExceptionFactory.get(apiError.getErrorId());

		if (defExcp != null) {
			throw defExcp.getInstance(apiError);
		}

	}
}
