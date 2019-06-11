package com.amx.jax.model.customer;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.request.CustomerInfoRequest;

public class CreateCustomerInfoRequest extends CustomerInfoRequest {

	@NotNull(message = "documentUploadReference may not be null")
	List<BigDecimal> documentUploadReference;
	@NotNull
	Boolean pepsIndicator;

	public List<BigDecimal> getDocumentUploadReference() {
		return documentUploadReference;
	}

	public void setDocumentUploadReference(List<BigDecimal> documentUploadReference) {
		this.documentUploadReference = documentUploadReference;
	}

	public Boolean getPepsIndicator() {
		return pepsIndicator;
	}

	public void setPepsIndicator(Boolean pepsIndicator) {
		this.pepsIndicator = pepsIndicator;
	}
}
