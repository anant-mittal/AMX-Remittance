package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class JaxMetaParameter {

	BigDecimal newBeneTransactionTimeLimit;
	BigDecimal maxDomAmountLimit;

	public BigDecimal getNewBeneTransactionTimeLimit() {
		return newBeneTransactionTimeLimit;
	}

	public void setNewBeneTransactionTimeLimit(BigDecimal newBeneTransactionTimeLimit) {
		this.newBeneTransactionTimeLimit = newBeneTransactionTimeLimit;
	}

	public BigDecimal getMaxDomAmountLimit() {
		return maxDomAmountLimit;
	}

	public void setMaxDomAmountLimit(BigDecimal maxDomAmountLimit) {
		this.maxDomAmountLimit = maxDomAmountLimit;
	}
}
