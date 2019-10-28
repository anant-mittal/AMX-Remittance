package com.amx.jax.client.compliance;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class RejectDocRequest {

	@ApiModelProperty(example = "BANK_STATEMENT")
	@NotNull
	String documentType;
	@ApiModelProperty(example = "HVT_LOCAL_TRNX")
	@NotNull
	String documentCategory;
	@ApiModelProperty(example = "4228")
	@NotNull
	BigDecimal remittanceTransactionId;
	@ApiModelProperty(example = "invalid document")
	String rejectReason;
	@ApiModelProperty(example = "DRIVING_LICENSE")
	@NotNull
	String newDocumentType;

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

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getNewDocumentType() {
		return newDocumentType;
	}

	public void setNewDocumentType(String newDocumentType) {
		this.newDocumentType = newDocumentType;
	}
}
