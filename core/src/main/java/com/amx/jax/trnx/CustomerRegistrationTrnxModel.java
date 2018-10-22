package com.amx.jax.trnx;

import java.io.Serializable;
import java.util.List;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.CustomerCredential;
import com.amx.jax.model.OtpData;
import com.amx.jax.model.request.CustomerPersonalDetail;

public class CustomerRegistrationTrnxModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CustomerPersonalDetail customerPersonalDetail;
	CustomerHomeAddress customerHomeAddress;
	List<SecurityQuestionModel> securityquestions;

	String caption;
	String imageUrl;

	CustomerCredential customerCredential;

	OtpData otpData;

	public CustomerPersonalDetail getCustomerPersonalDetail() {
		return customerPersonalDetail;
	}

	public void setCustomerPersonalDetail(CustomerPersonalDetail customerPersonalDetail) {
		this.customerPersonalDetail = customerPersonalDetail;
	}

	public OtpData getOtpData() {
		return otpData;
	}

	public void setOtpData(OtpData otpData) {
		this.otpData = otpData;
	}

	public CustomerHomeAddress getCustomerHomeAddress() {
		return customerHomeAddress;
	}

	public void setCustomerHomeAddress(CustomerHomeAddress customerHomeAddress) {
		this.customerHomeAddress = customerHomeAddress;
	}

	public List<SecurityQuestionModel> getSecurityquestions() {
		return securityquestions;
	}

	public void setSecurityquestions(List<SecurityQuestionModel> securityquestions) {
		this.securityquestions = securityquestions;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public CustomerCredential getCustomerCredential() {
		return customerCredential;
	}

	public void setCustomerCredential(CustomerCredential customerCredential) {
		this.customerCredential = customerCredential;
	}

}
