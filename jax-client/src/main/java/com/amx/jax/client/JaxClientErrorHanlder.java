package com.amx.jax.client;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxApplicationException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.utils.JsonUtil;

@Component
public class JaxClientErrorHanlder implements ResponseErrorHandler {

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
		AmxApiError apiError = JsonUtil.fromJson(apiErrorJson, AmxApiError.class);
		checkUnknownJaxError(apiError);
		checkInvalidInputErrors(apiError);
		checkCustomerValidationErrors(apiError);
		checkAlreadyExistsError(apiError);
		checkIncorrectInputError(apiError);
		checkResourceNotFoundException(apiError);
		validateRemittanceDataValidation(apiError);
		validateLimitExeededException(apiError);
		throw new JaxApplicationException(apiError);
	}

	private void validateLimitExeededException(AmxApiError error) {
		if (JaxError.SEND_OTP_LIMIT_EXCEEDED.getCode().equals(error.getErrorId())) {
			throw new LimitExeededException(error);
		}
	}

	private void checkUnknownJaxError(AmxApiError apiError) {
		if (JaxError.UNKNOWN_JAX_ERROR.getCode().equals(apiError.getErrorId())) {
			throw new UnknownJaxError(apiError);
		}
	}

	protected void checkInvalidInputErrors(AmxApiError error) throws InvalidInputException {
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

	protected void checkCustomerValidationErrors(AmxApiError error)
			throws CustomerValidationException, LimitExeededException {
		if (error != null) {

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
			if (JaxError.USER_NOT_REGISTERED.getCode().equals(error.getErrorId())) {
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
			if (JaxError.INVALID_CUSTOMER_REFERENCE.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.OLD_EMOS_USER_DELETED.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.OLD_EMOS_USER_DATA_EXPIRED.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.ID_PROOFS_IMAGES_NOT_FOUND.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.OTP_EXPIRED.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (JaxError.INCORRECT_LENGTH.getCode().equals(error.getErrorId())) {
				iscustValidationError = true;
			}
			if (iscustValidationError) {
				throw new CustomerValidationException(error);
			}
		}
	}

	protected void checkAlreadyExistsError(AmxApiError error) throws AlreadyExistsException {

		if (error != null) {

			if (JaxError.USERNAME_ALREADY_EXISTS.getCode().equals(error.getErrorId())) {
				throw new AlreadyExistsException(error);
			}

			if (JaxError.ALREADY_EXIST.getCode().equals(error.getErrorId())) {
				throw new AlreadyExistsException(error);
			}
		}
	}

	protected void checkIncorrectInputError(AmxApiError error) throws IncorrectInputException {

		if (error != null) {

			if (JaxError.WRONG_PASSWORD.getCode().equals(error.getErrorId())) {
				throw new IncorrectInputException(error);
			}
			if (JaxError.INVALID_OTP.getCode().equals(error.getErrorId())) {
				throw new IncorrectInputException(error);
			}
			if (JaxError.INCORRECT_SECURITY_QUESTION_ANSWER.getCode().equals(error.getErrorId())) {
				throw new IncorrectInputException(error);
			}
			if (JaxError.MISSING_OTP.getCode().equals(error.getErrorId())) {
				throw new IncorrectInputException(error);
			}
		}
	}

	protected void checkResourceNotFoundException(AmxApiError error) throws ResourceNotFoundException {
		if (error != null) {

			if (JaxError.EXCHANGE_RATE_NOT_FOUND.getCode().equals(error.getErrorId())) {
				throw new ResourceNotFoundException(error);
			}
		}
	}

	protected void validateRemittanceDataValidation(AmxApiError error)
			throws RemittanceTransactionValidationException, LimitExeededException {

		if (error != null) {

			if (JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL.getCode().equals(error.getErrorId())) {
				throw new RemittanceTransactionValidationException(error);
			}
			if (JaxError.NO_OF_TRANSACTION_LIMIT_EXCEEDED.getCode().equals(error.getErrorId())) {
				throw new LimitExeededException(error);
			}
			if (JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED.getCode().equals(error.getErrorId())) {
				throw new LimitExeededException(error);
			}
			if (JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_PER_BENE.getCode().equals(error.getErrorId())) {
				throw new LimitExeededException(error);
			}

			if (JaxError.TRANSACTION_HISTORY_NOT_FOUND.getCode().equals(error.getErrorId())) {
				throw new RemittanceTransactionValidationException(error);
			}

		}
	}
}
