package com.amx.jax.client;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.model.response.ApiError;

@Component
public class JaxClientErrorHanlder implements ResponseErrorHandler {

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
	public void handleError(ClientHttpResponse response) throws IOException, InvalidInputException {

		List<String> errorCodes = response.getHeaders().get("ERROR_CODE");
		List<String> errorMessages = response.getHeaders().get("ERROR_MESSAGE");
		ApiError apiError = new ApiError();
		if (errorCodes != null && !errorCodes.isEmpty()) {
			apiError.setErrorId(errorCodes.get(0));
		}
		if (errorMessages != null && !errorMessages.isEmpty()) {
			apiError.setErrorMessage(errorMessages.get(0));
		}
		checkInvalidInputErrors(apiError);

	}

	protected void checkInvalidInputErrors(ApiError error) throws InvalidInputException {
		if (error != null) {
			if (JaxError.INVALID_CIVIL_ID.getCode().equals(error.getErrorId())) {
				throw new InvalidInputException(error);
			}
			if (JaxError.CUSTOMER_NOT_FOUND.getCode().equals(error.getErrorId())) {
				throw new InvalidInputException(error);
			}
			if (JaxError.NULL_CUSTOMER_ID.getCode().equals(error.getErrorId())) {
				throw new InvalidInputException(error);
			}
			if (JaxError.INVALID_EXCHANGE_AMOUNT.getCode().equals(error.getErrorId())) {
				throw new InvalidInputException(error);
			}
		}
	}
}
