package com.amx.jax.payg.codes;

import com.amx.jax.dict.PayGCodes.CodeCategory;
import com.amx.jax.dict.PayGCodes.IPayGCode;

public enum OmanNetCodes implements IPayGCode {

	CAPTURED(CodeCategory.TXN_SUCCESS, "Transaction was approved"),

	VOIDED(CodeCategory.TXN_DATA, "Transaction was voided"),

	NOT_CAPTURED(CodeCategory.TXN_AUTH, "Transaction was not approved"),

	CANCELED(CodeCategory.TXN_CANCEL_SUCC, "Canceled Transaction"),

	GENERIC_ERROR(CodeCategory.TXN_DATA), UNKNOWN(CodeCategory.UNKNOWN);

	CodeCategory category;
	String description;

	OmanNetCodes(CodeCategory category, String description) {
		this.category = category;
		this.description = description;
	}

	OmanNetCodes(CodeCategory category) {
		this.category = category;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public CodeCategory getCategory() {
		return category;
	}

}
