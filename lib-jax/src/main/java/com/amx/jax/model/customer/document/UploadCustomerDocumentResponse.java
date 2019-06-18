package com.amx.jax.model.customer.document;

import java.math.BigDecimal;

public class UploadCustomerDocumentResponse {

	BigDecimal uploadReference;
	
	public UploadCustomerDocumentResponse() {
		super();
	}

	public UploadCustomerDocumentResponse(BigDecimal uploadReference) {
		super();
		this.uploadReference = uploadReference;
	}

	public BigDecimal getUploadReference() {
		return uploadReference;
	}

	public void setUploadReference(BigDecimal uploadReference) {
		this.uploadReference = uploadReference;
	}
}
