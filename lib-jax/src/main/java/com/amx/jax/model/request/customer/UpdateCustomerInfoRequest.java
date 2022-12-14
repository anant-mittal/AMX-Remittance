package com.amx.jax.model.request.customer;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import com.amx.jax.model.request.UpdateCustomerAddressDetailRequest;
import com.amx.jax.model.request.UpdateCustomerEmploymentDetailsReq;
import com.amx.jax.model.request.UpdateCustomerPersonalDetailRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UpdateCustomerInfoRequest implements CustomerDocValidationData {

	@Valid
	UpdateCustomerPersonalDetailRequest personalDetailInfo;
	@Valid
	UpdateCustomerAddressDetailRequest homeAddressDetail;
	@Valid
	UpdateCustomerAddressDetailRequest localAddressDetail;
	@Valid
	UpdateCustomerEmploymentDetailsReq employmentDetail;

	List<BigDecimal> documentUploadReference;
	
	@JsonIgnore
	boolean isCalledFromAddApi = false;

	public UpdateCustomerPersonalDetailRequest getPersonalDetailInfo() {
		return personalDetailInfo;
	}

	public void setPersonalDetailInfo(UpdateCustomerPersonalDetailRequest personalDetailInfo) {
		this.personalDetailInfo = personalDetailInfo;
	}

	public UpdateCustomerAddressDetailRequest getHomeAddressDetail() {
		return homeAddressDetail;
	}

	public void setHomeAddressDetail(UpdateCustomerAddressDetailRequest homeAddressDetail) {
		this.homeAddressDetail = homeAddressDetail;
	}

	public UpdateCustomerAddressDetailRequest getLocalAddressDetail() {
		return localAddressDetail;
	}

	public void setLocalAddressDetail(UpdateCustomerAddressDetailRequest localAddressDetail) {
		this.localAddressDetail = localAddressDetail;
	}

	public UpdateCustomerEmploymentDetailsReq getEmploymentDetail() {
		return employmentDetail;
	}

	public void setEmploymentDetail(UpdateCustomerEmploymentDetailsReq employmentDetail) {
		this.employmentDetail = employmentDetail;
	}

	public List<BigDecimal> getDocumentUploadReference() {
		return documentUploadReference;
	}

	public void setDocumentUploadReference(List<BigDecimal> documentUploadReference) {
		this.documentUploadReference = documentUploadReference;
	}

	@Override
	@JsonIgnore
	public BigDecimal getIncomeRangeId() {
		return employmentDetail != null ? employmentDetail.getIncomeRangeId() : null;
	}

	@Override
	@JsonIgnore
	public String getEmployer() {
		return employmentDetail != null ? employmentDetail.getEmployer() : null;
	}

	@Override
	@JsonIgnore
	public BigDecimal getArticleDetailsId() {
		return employmentDetail != null ? employmentDetail.getArticleDetailsId() : null;
	}

	@Override
	@JsonIgnore
	public Boolean isCreateCustomerRequest() {
		return false;
	}

	@Override
	@JsonIgnore
	public String getIdentityInt() {
		return null;
	}

	@Override
	@JsonIgnore
	public BigDecimal getIdentityTypeId() {
		return null;
	}

	@Override
	public Boolean isLocalAddressChange() {
		return this.localAddressDetail != null;
	}

	@JsonIgnore
	public boolean isCalledFromAddApi() {
		return isCalledFromAddApi;
	}

	public void setCalledFromAddApi(boolean isCalledFromAddApi) {
		this.isCalledFromAddApi = isCalledFromAddApi;
	}
}
