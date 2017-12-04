package com.amx.jax.client;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiError;
import com.amx.amxlib.model.response.ApiResponse;

@Component
public abstract class AbstractJaxServiceClient {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	@Qualifier("base_url")
	protected URL baseUrl;

	protected MultiValueMap<String, String> getHeader() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("meta-info", "{\"country-id\":" + jaxMetaInfo.getCountryId().intValue() + "}");
		headers.add("meta-info", "{\"company-id\":" + jaxMetaInfo.getCompanyId().intValue() + "}");
		return headers;
	}

	protected void checkInvalidInputErrors(ApiResponse<?> response) throws InvalidInputException {
		if (response.getError() != null) {
			ApiError error = response.getError().get(0);
			if (JaxError.INVALID_CIVIL_ID.getCode().equals(error.getErrorId())) {
				throw new InvalidInputException(error);
			}
			if (JaxError.CUSTOMER_NOT_FOUND.getCode().equals(error.getErrorId())) {
				throw new InvalidInputException(error);
			}
		}
	}

	protected void checkCustomerValidationErrors(ApiResponse<?> response)
			throws CustomerValidationException, LimitExeededException {
		if (response.getError() != null) {
			ApiError error = response.getError().get(0);
			if (JaxError.BLACK_LISTED_CUSTOMER.getCode().equals(error.getErrorId())) {
				throw new CustomerValidationException(error);
			}
			if (JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.getCode().equals(error.getErrorId())) {
				throw new LimitExeededException(error);
			}
		}
	}

	protected void checkAlreadyExistsError(ApiResponse<?> response) throws AlreadyExistsException {

		if (response.getError() != null) {
			ApiError error = response.getError().get(0);
			if (JaxError.USERNAME_ALREADY_EXISTS.getCode().equals(error.getErrorId())) {
				throw new AlreadyExistsException(error);
			}
		}
	}

	protected void checkIncorrectInputError(ApiResponse<?> response) throws IncorrectInputException {

		if (response.getError() != null) {
			ApiError error = response.getError().get(0);
			if (JaxError.WRONG_PASSWORD.getCode().equals(error.getErrorId())) {
				throw new IncorrectInputException(error);
			}
			if (JaxError.INVALID_OTP.getCode().equals(error.getErrorId())) {
				throw new IncorrectInputException(error);
			}
			if (JaxError.INCORRECT_SECURITY_QUESTION_ANSWER.getCode().equals(error.getErrorId())) {
				throw new IncorrectInputException(error);
			}
		}
	}

}
