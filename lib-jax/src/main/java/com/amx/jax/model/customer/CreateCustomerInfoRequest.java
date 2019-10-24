package com.amx.jax.model.customer;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.customer.CustomerDocValidationData;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CreateCustomerInfoRequest extends CustomerInfoRequest implements CustomerDocValidationData {

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

	@Override
	@JsonIgnore
	public String getEmployer() {
		return super.getCustomerEmploymentDetails().getEmployer();
	}

	@Override
	@JsonIgnore
	public BigDecimal getIncomeRangeId() {
		return super.getCustomerEmploymentDetails().getIncomeRangeId();
	}

	@Override
	@JsonIgnore
	public BigDecimal getArticleDetailsId() {
		return super.getCustomerEmploymentDetails().getArticleDetailsId();
	}

	@Override
	@JsonIgnore
	public Boolean isCreateCustomerRequest() {
		return true;
	}

	@Override
	@JsonIgnore
	public String getIdentityInt() {
		return super.getCustomerPersonalDetail().getIdentityInt();
	}

	@Override
	@JsonIgnore
	public BigDecimal getIdentityTypeId() {
		return super.getCustomerPersonalDetail().getIdentityTypeId();
	}

	@Override
	public Boolean isLocalAddressChange() {
		return super.getLocalAddressDetails() != null;
	}
}
