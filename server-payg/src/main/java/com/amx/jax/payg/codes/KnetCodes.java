package com.amx.jax.payg.codes;

import com.amx.jax.payg.PayGCodes.CodeCategory;
import com.amx.jax.payg.PayGCodes.IPayGCode;

public enum KnetCodes implements IPayGCode {

	CAPTURED(CodeCategory.TXN_SUCCESS, "Transaction was approved"),

	UNKNOWN(CodeCategory.UNKNOWN);

	CodeCategory category;
	String description;

	KnetCodes(CodeCategory category, String description) {
		this.category = category;
		this.description = description;
	}

	KnetCodes(CodeCategory category) {
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
