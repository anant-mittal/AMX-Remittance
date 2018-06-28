package com.amx.amxlib.meta.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class JaxMetaParameter implements Serializable {

	private static final long serialVersionUID = -1863361054395335347L;

	BigDecimal newBeneTransactionTimeLimit;
	BigDecimal maxDomAmountLimit;
	BigDecimal applicationCountryId;

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

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

}
