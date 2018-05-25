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

	private Boolean isActive;

	private PersonInfo personinfo;

	private String motp;

	private String eotp;

	private List<SecurityQuestionModel> verificationAnswers;

	private boolean isRegistrationFlow;

	public boolean isRegistrationFlow() {
		return isRegistrationFlow;
	}

	public void setRegistrationFlow(boolean isRegistrationFlow) {
		this.isRegistrationFlow = isRegistrationFlow;
	}

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public PersonInfo getPersoninfo() {
		return personinfo;
	}

	public void setPersoninfo(PersonInfo personinfo) {
		this.personinfo = personinfo;
	}

	public String getMotp() {
		return motp;
	}

	public void setMotp(String motp) {
		this.motp = motp;
	}

	public String getEotp() {
		return eotp;
	}

	public void setEotp(String eotp) {
		this.eotp = eotp;
	}

	@Override
	public String toString() {
		return "CustomerModel [identityId=" + identityId + ", email=" + email + ", mobile=" + mobile + ", loginId="
				+ loginId + ", password=" + password + ", securityquestions=" + securityquestions + ", caption="
				+ caption + ", imageUrl=" + imageUrl + ", customerId=" + customerId + ", isActive=" + isActive
				+ ", personinfo=" + personinfo + ", motp=" + motp + ", eotp=" + eotp + "]";
	}

	public List<SecurityQuestionModel> getVerificationAnswers() {
		return verificationAnswers;
	}

	public void setVerificationAnswers(List<SecurityQuestionModel> verificationAnswers) {
		this.verificationAnswers = verificationAnswers;
	}

}
