package com.amx.jax.model.request.customer;

import com.amx.jax.model.request.UpdateCustomerAddressDetailRequest;
import com.amx.jax.model.request.UpdateCustomerEmploymentDetailsReq;
import com.amx.jax.model.request.UpdateCustomerPersonalDetailRequest;

public class UpdateCustomerInfoRequest {

	UpdateCustomerPersonalDetailRequest personalDetailInfo;
	UpdateCustomerAddressDetailRequest homeAddressDetail;
	UpdateCustomerAddressDetailRequest localAddressDetail;
	UpdateCustomerEmploymentDetailsReq employmentDetail;
	
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
	
	
}
