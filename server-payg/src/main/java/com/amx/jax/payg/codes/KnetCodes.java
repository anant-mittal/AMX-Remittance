package com.amx.jax.payg.codes;

import com.amx.jax.payg.PayGCodes.CodeCategory;
import com.amx.jax.payg.PayGCodes.IPayGCode;

public enum KnetCodes implements IPayGCode {

	CAPTURED(CodeCategory.TXN_SUCCESS, "Transaction was approved"),

	UNKNOWN(CodeCategory.UNKNOWN),
	
	VOIDED(CodeCategory.TXN_DATA,"Transaction was voided"),
	
	NOT_CAPTURED(CodeCategory.TXN_AUTH,"Transaction was not approved"),
	
	CANCELED(CodeCategory.TXN_CANCEL_SUCC,"Canceled Transaction"),

	GENERIC_ERROR(CodeCategory.TXN_DATA);

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
