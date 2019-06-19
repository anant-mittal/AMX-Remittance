package com.amx.jax.model.request.customer;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.request.UpdateCustomerAddressDetailRequest;
import com.amx.jax.model.request.UpdateCustomerEmploymentDetailsReq;
import com.amx.jax.model.request.UpdateCustomerPersonalDetailRequest;

public class UpdateCustomerInfoRequest implements CustomerDocValidationData {

	UpdateCustomerPersonalDetailRequest personalDetailInfo;
	UpdateCustomerAddressDetailRequest homeAddressDetail;
	UpdateCustomerAddressDetailRequest localAddressDetail;
	UpdateCustomerEmploymentDetailsReq employmentDetail;

	List<BigDecimal> documentUploadReference;

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
	public BigDecimal getIncomeRangeId() {
		return employmentDetail != null ? employmentDetail.getIncomeRangeId() : null;
	}

	@Override
	public String getEmployer() {
		return employmentDetail != null ? employmentDetail.getEmployer() : null;
	}

	@Override
	public BigDecimal getArticleDetailsId() {
		return employmentDetail != null ? employmentDetail.getArticleDetailsId() : null;
	}

	@Override
	public Boolean isCreateCustomerRequest() {
		return false;
	}

	@Override
	public String getIdentityInt() {
		return null;
	}

	@Override
	public BigDecimal getIdentityTypeId() {
		return null;
	}
}
