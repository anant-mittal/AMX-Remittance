package com.amx.jax.dict;

import com.amx.utils.ArgUtil;

public class PayGCodes {

	public static enum CodeTypes {
		TECHNICAL, VALIDATION, UNCERTAIN
	}

	public static enum CodeStatus {
		APPROVED, DECLINED, UNKNOWN
	}

	public static enum CodeCategory {
		CONN_FAILURE(CodeTypes.TECHNICAL, CodeStatus.DECLINED,
				"The transaction could not be completed due to connectivity issue"),

		FORMT_ERR(CodeTypes.TECHNICAL, CodeStatus.DECLINED,
				"The transaction could not be completed due to ISO Message Formatter error"),

		TXN_SUCCESS(CodeTypes.VALIDATION, CodeStatus.APPROVED, "The transaction is successfully completed"),

		UNKNOWN(CodeTypes.UNCERTAIN, CodeStatus.APPROVED, "Not able to determine"),

		TXN_DATA(CodeTypes.VALIDATION, CodeStatus.DECLINED,
				"The transaction could not be completed due to invalid/missing transaction data"),

		TXN_AUTH(CodeTypes.VALIDATION, CodeStatus.DECLINED,
				"The transaction could not be completed due to authentication failure"),

		TXN_CANCEL_SUCC(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction has been cancelled"),
		
		INST_ISSUES(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to missing/invalid institution details"),
		LOG_NOT_FOUND(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The transaction could not be completed due to missing log details"),
		MRCH_ERR(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to missing/invalid merchant details"),
		PAYMENT_ERR(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to missing/invalid payment details"),
		TERML_ERR(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing terminal details"),
		TXN_OTP_LIM(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to OTP attempt limit exceeded"),
		TXN_OTP_VLDT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to OTP validity expired"),
		TXN_AUTH_PIN(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid PIN"),
		TXN_BANK(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be completed due to some technical error"),
		TXN_CANCEL_FAIL(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be cancelled"),
		TXN_CARD(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing card details"),
		TXN_CARD_VLDT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to card validity expiry"),
		TXN_DATA_CURR(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be completed due to currency conversion failure"),
		TXN_DN_RISK(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to risk check failure"),
		TXN_LIMIT_FUNDS(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to insufficient funds"),
		TXN_LIMIT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to the withdrwal limit exceeded"),
		TXN_REFUND_ISSUE(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The refund request could not be completed due to technical error"),
		TXN_REQ_FAILURE(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The transaction could not be processed"),
		TXN_SEC_FAILURE(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The transaction could not be completed due to unsecured network"),
		TXN_SESSION(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "Transaction timeout occurred"),
		TXN_TRNP(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing transportal details"),
		TXN_UDF(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing user-defined details"),
		TXN_URL(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing URL response");

		CodeTypes type;
		CodeStatus status;
		String message;

		CodeCategory(CodeTypes type, CodeStatus status, String message) {
			this.type = type;
			this.message = message;
			this.status = status;
		}

		public CodeTypes getType() {
			return type;
		}

		public CodeStatus getStatus() {
			return status;
		}

	}

	public interface IPayGCode {
		public String getDescription();

		public CodeCategory getCategory();
	}

	@SuppressWarnings("rawtypes")
	public static Enum getPayGCode(Object code, Enum defaultCode) {
		if (ArgUtil.isEmpty(code)) {
			return defaultCode;
		}
		return ArgUtil.parseAsEnum(code, defaultCode);
	}

}
