package com.amx.amxlib.exception;

import com.amx.amxlib.error.JaxError;
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

		if (JaxError.UNKNOWN_JAX_ERROR.getCode().equals(apiError.getErrorId())) {
			return new UnknownJaxError(apiError);
		}

		// checkInvalidInputErrors
		if (JaxError.INVALID_CIVIL_ID.getCode().equals(apiError.getErrorId())) {
			return new InvalidInputException(apiError);
		}
		if (JaxError.CUSTOMER_NOT_FOUND.getCode().equals(apiError.getErrorId())) {
			return new InvalidInputException(apiError);
		}
		if (JaxError.NULL_CUSTOMER_ID.getCode().equals(apiError.getErrorId())) {
			return new InvalidInputException(apiError);
		}
		if (JaxError.INVALID_EXCHANGE_AMOUNT.getCode().equals(apiError.getErrorId())) {
			return new InvalidInputException(apiError);
		}

		// checkCustomerValidationErrors
		if (JaxError.BLACK_LISTED_CUSTOMER.getCode().equals(apiError.getErrorId())) {
			return new CustomerValidationException(apiError);
		}
		if (JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.getCode().equals(apiError.getErrorId())) {
			return new LimitExeededException(apiError);
		}
		boolean iscustValidationError = false;
		if (JaxError.USER_NOT_FOUND.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.USER_NOT_REGISTERED.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOF_EXPIRED.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOFS_NOT_VALID.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOFS_SCAN_NOT_FOUND.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.CUSTOMER__SIGNATURE_UNAVAILABLE.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.INVALID_INSURANCE_INDICATOR.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.OLD_EMOS_USER_NOT_FOUND.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.MISSING_CONTACT_DETAILS.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.MISSING_HOME_CONTACT_DETAILS.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.INVALID_CUSTOMER_REFERENCE.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.OLD_EMOS_USER_DELETED.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.OLD_EMOS_USER_DATA_EXPIRED.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.ID_PROOFS_IMAGES_NOT_FOUND.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.OTP_EXPIRED.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (JaxError.INCORRECT_LENGTH.getCode().equals(apiError.getErrorId())) {
			iscustValidationError = true;
		}
		if (iscustValidationError) {
			return new CustomerValidationException(apiError);
		}

		// checkAlreadyExistsError
		if (JaxError.USERNAME_ALREADY_EXISTS.getCode().equals(apiError.getErrorId())) {
			return new AlreadyExistsException(apiError);
		}

		if (JaxError.ALREADY_EXIST.getCode().equals(apiError.getErrorId())) {
			return new AlreadyExistsException(apiError);
		}

		// checkIncorrectInputError
		if (JaxError.WRONG_PASSWORD.getCode().equals(apiError.getErrorId())) {
			return new IncorrectInputException(apiError);
		}
		if (JaxError.INVALID_OTP.getCode().equals(apiError.getErrorId())) {
			return new IncorrectInputException(apiError);
		}
		if (JaxError.INCORRECT_SECURITY_QUESTION_ANSWER.getCode().equals(apiError.getErrorId())) {
			return new IncorrectInputException(apiError);
		}
		if (JaxError.MISSING_OTP.getCode().equals(apiError.getErrorId())) {
			return new IncorrectInputException(apiError);
		}

		// checkResourceNotFoundException
		if (JaxError.EXCHANGE_RATE_NOT_FOUND.getCode().equals(apiError.getErrorId())) {
			return new ResourceNotFoundException(apiError);
		}

		// validateRemittanceDataValidation
		if (JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL.getCode().equals(apiError.getErrorId())) {
			return new RemittanceTransactionValidationException(apiError);
		}
		if (JaxError.NO_OF_TRANSACTION_LIMIT_EXCEEDED.getCode().equals(apiError.getErrorId())) {
			return new LimitExeededException(apiError);
		}
		if (JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED.getCode().equals(apiError.getErrorId())) {
			return new LimitExeededException(apiError);
		}
		if (JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_PER_BENE.getCode().equals(apiError.getErrorId())) {
			return new LimitExeededException(apiError);
		}

		if (JaxError.TRANSACTION_HISTORY_NOT_FOUND.getCode().equals(apiError.getErrorId())) {
			return new RemittanceTransactionValidationException(apiError);
		}

		// validateLimitExeededException
		if (JaxError.SEND_OTP_LIMIT_EXCEEDED.getCode().equals(apiError.getErrorId())) {
			return new LimitExeededException(apiError);
		}

		return new JaxApplicationException(apiError);
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return null;
	}

	@Override
	public void deserializeMeta(AmxApiError amxApiError) {
		// TODO Auto-generated method stub
		
	}

}
