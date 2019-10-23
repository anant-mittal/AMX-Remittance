package com.amx.amxlib.exception;

import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;

public class CommonJaxException extends AmxApiException {

	private static final long serialVersionUID = 1L;

	public CommonJaxException(AmxApiError error) {
		super(error);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {

		if (error == null) {
			return new JaxApplicationException(apiError);
		}

		if (JaxError.UNKNOWN_JAX_ERROR.getStatusKey().equals(apiError.getStatusKey())) {
			return new UnknownJaxError(apiError);
		}

		// checkInvalidInputErrors
		if (JaxError.INVALID_CIVIL_ID.getStatusKey().equals(apiError.getStatusKey())) {
			return new InvalidInputException(apiError);
		}
		if (JaxError.CUSTOMER_NOT_FOUND.getStatusKey().equals(apiError.getStatusKey())) {
			return new InvalidInputException(apiError);
		}
		if (JaxError.NULL_CUSTOMER_ID.getStatusKey().equals(apiError.getStatusKey())) {
			return new InvalidInputException(apiError);
		}
		if (JaxError.INVALID_EXCHANGE_AMOUNT.getStatusKey().equals(apiError.getStatusKey())) {
			return new InvalidInputException(apiError);
		}

		// checkCustomerValidationErrors
		if (JaxError.BLACK_LISTED_CUSTOMER.getStatusKey().equals(apiError.getStatusKey())) {
			return new CustomerValidationException(apiError);
		}
		if (JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.getStatusKey().equals(apiError.getStatusKey())) {
			return new LimitExeededException(apiError);
		}
		boolean iscustValidationError = false;
		if (JaxError.USER_NOT_FOUND.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.USER_NOT_REGISTERED.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOF_EXPIRED.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOFS_NOT_VALID.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOFS_SCAN_NOT_FOUND.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.CUSTOMER_SIGNATURE_UNAVAILABLE.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.INVALID_INSURANCE_INDICATOR.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.OLD_EMOS_USER_NOT_FOUND.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.MISSING_CONTACT_DETAILS.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.MISSING_HOME_CONTACT_DETAILS.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.INVALID_CUSTOMER_REFERENCE.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.OLD_EMOS_USER_DELETED.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.OLD_EMOS_USER_DATA_EXPIRED.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOFS_IMAGES_NOT_FOUND.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.OTP_EXPIRED.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (JaxError.INCORRECT_LENGTH.getStatusKey().equals(apiError.getStatusKey())) {
			iscustValidationError = true;
		}
		if (iscustValidationError) {
			return new CustomerValidationException(apiError);
		}

		// checkAlreadyExistsError
		if (JaxError.USERNAME_ALREADY_EXISTS.getStatusKey().equals(apiError.getStatusKey())) {
			return new AlreadyExistsException(apiError);
		}

		if (JaxError.ALREADY_EXIST.getStatusKey().equals(apiError.getStatusKey())) {
			return new AlreadyExistsException(apiError);
		}

		// checkIncorrectInputError
		if (JaxError.WRONG_PASSWORD.getStatusKey().equals(apiError.getStatusKey())) {
			return new IncorrectInputException(apiError);
		}
		if (JaxError.INVALID_OTP.getStatusKey().equals(apiError.getStatusKey())) {
			return new IncorrectInputException(apiError);
		}
		if (JaxError.INCORRECT_SECURITY_QUESTION_ANSWER.getStatusKey().equals(apiError.getStatusKey())) {
			return new IncorrectInputException(apiError);
		}
		if (JaxError.MISSING_OTP.getStatusKey().equals(apiError.getStatusKey())) {
			return new IncorrectInputException(apiError);
		}

		// checkResourceNotFoundException
		if (JaxError.EXCHANGE_RATE_NOT_FOUND.getStatusKey().equals(apiError.getStatusKey())) {
			return new ResourceNotFoundException(apiError);
		}

		// validateRemittanceDataValidation
		if (JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL.getStatusKey().equals(apiError.getStatusKey())) {
			return new RemittanceTransactionValidationException(apiError);
		}
		if (JaxError.NO_OF_TRANSACTION_LIMIT_EXCEEDED.getStatusKey().equals(apiError.getStatusKey())) {
			return new LimitExeededException(apiError);
		}
		if (JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED.getStatusKey().equals(apiError.getStatusKey())) {
			return new LimitExeededException(apiError);
		}
		if (JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_PER_BENE.getStatusKey().equals(apiError.getStatusKey())) {
			return new LimitExeededException(apiError);
		}

		if (JaxError.TRANSACTION_HISTORY_NOT_FOUND.getStatusKey().equals(apiError.getStatusKey())) {
			return new RemittanceTransactionValidationException(apiError);
		}

		// validateLimitExeededException
		if (JaxError.SEND_OTP_LIMIT_EXCEEDED.getStatusKey().equals(apiError.getStatusKey())) {
			return new LimitExeededException(apiError);
		}

		return new JaxApplicationException(apiError);
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return null;
	}

	@Override
	public boolean isReportable() {
		return false;
	}

}
