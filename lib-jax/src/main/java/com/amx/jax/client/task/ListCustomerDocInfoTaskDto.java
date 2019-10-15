package com.amx.jax.client.task;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ListCustomerDocInfoTaskDto {

	@ApiMockModelProperty(example = "INCOME_PROOF")
	String documentCategory;

	@ApiMockModelProperty(example = "LOAN_STATEMENT")
	String documentType;

	BigDecimal remittanceTransactionId;
	
	BigDecimal customerId;
	
	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}

	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
}
