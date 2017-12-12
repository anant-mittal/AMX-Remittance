package com.amx.jax.client;

import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.response.ApiError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class AbstractJaxServiceClient {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	@Qualifier("base_url")
	protected URL baseUrl;

	private Logger log = Logger.getLogger(AbstractJaxServiceClient.class);

	protected MultiValueMap<String, String> getHeader() {

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		try {
			JaxMetaInfo info = new JaxMetaInfo();
			info.setCountryId(jaxMetaInfo.getCountryId());
			info.setChannel(jaxMetaInfo.getChannel());
			info.setCompanyId(jaxMetaInfo.getCompanyId());
			info.setCustomerId(jaxMetaInfo.getCustomerId());
			headers.add("meta-info", new ObjectMapper().writeValueAsString(info));
		} catch (JsonProcessingException e) {
			log.error("error in getheader of jaxclient", e);
		}
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
			if (JaxError.NULL_CUSTOMER_ID.getCode().equals(error.getErrorId())) {
				throw new InvalidInputException(error);
			}
			if (JaxError.INVALID_EXCHANGE_AMOUNT.getCode().equals(error.getErrorId())) {
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
			boolean iscustValidationError = false;
			if (JaxError.USER_NOT_FOUND.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.ID_PROOF_EXPIRED.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.ID_PROOFS_NOT_VALID.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.ID_PROOFS_SCAN_NOT_FOUND.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.CUSTOMER__SIGNATURE_UNAVAILABLE.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.INVALID_INSURANCE_INDICATOR.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.OLD_EMOS_USER_NOT_FOUND.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.MISSING_CONTACT_DETAILS.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.MISSING_HOME_CONTACT_DETAILS.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.MISSING_LOCAL_CONTACT_DETAILS.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}

			if (iscustValidationError) {
				throw new CustomerValidationException(error);
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

	protected void checkResourceNotFoundException(ApiResponse<?> response) throws ResourceNotFoundException {
		if (response.getError() != null) {
			ApiError error = response.getError().get(0);
			if (JaxError.EXCHANGE_RATE_NOT_FOUND.getCode().equals(error.getErrorId())) {
				throw new ResourceNotFoundException(error);
			}
		}
	}
}
