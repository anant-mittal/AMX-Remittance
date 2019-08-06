package com.amx.jax.client.compliance;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class ApproveDocRequest {

	@ApiModelProperty(example = "BANK_STATEMENT")
	@NotNull
	String documentType;
	@ApiModelProperty(example = "HVT_LOCAL_TRNX")
	@NotNull
	String documentCategory;
	@ApiModelProperty(example = "4228")
	@NotNull
	BigDecimal remittanceTransactionId;

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}

	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}
}
