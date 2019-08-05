package com.amx.jax.client.task;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerDocUploadNotificationTaskData {

	@NotNull
	@ApiMockModelProperty(example = "KYC_PROOF")
	String documentCategory;

	@NotNull
	@ApiMockModelProperty(example = "CIVIL_ID")
	String documentType;

	@NotNull
	BigDecimal remittanceTransactionId;

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
}
