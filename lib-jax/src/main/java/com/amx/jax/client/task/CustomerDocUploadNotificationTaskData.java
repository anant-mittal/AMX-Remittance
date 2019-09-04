package com.amx.jax.client.task;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerDocUploadNotificationTaskData {

	@NotNull
	@ApiMockModelProperty(example = "HVT_LOCAL_TRNX")
	String documentCategory;

	@NotNull
	@ApiMockModelProperty(example = "[BANK_STATEMENT]")
	List<String> documentTypes;

	@NotNull
	@ApiMockModelProperty(example = "4228")
	BigDecimal remittanceTransactionId;

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

	public List<String> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<String> documentTypes) {
		this.documentTypes = documentTypes;
	}
}
