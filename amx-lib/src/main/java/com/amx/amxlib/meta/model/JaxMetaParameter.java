package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class JaxMetaParameter {

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
