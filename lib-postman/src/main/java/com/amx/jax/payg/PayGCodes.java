package com.amx.jax.payg;

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

		TXN_CANCEL_SUCC(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction has been cancelled");

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
