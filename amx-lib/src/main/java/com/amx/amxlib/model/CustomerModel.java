package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.List;


public class CustomerModel extends AbstractModel {

	private String identityId;

	private String email;

	private String mobile;

	private String loginId;

	private String password;

	private List<SecurityQuestionModel> securityquestions;

	private String caption;

	private String imageUrl;
	
	private BigDecimal customerId;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	@Override
	public String getModelType() {
		return "customer";
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

}
