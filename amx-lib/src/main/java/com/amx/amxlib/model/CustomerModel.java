package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.libjax.model.CustomerModelInterface.ICustomerModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerModel extends AbstractModel implements ICustomerModel {

	private static final long serialVersionUID = -8190742045911263443L;

	/**
	 * @deprecated contact Prashant T. to add identity id in this model
	 * 
	 */
	@Deprecated
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

	private CustomerFlags flags;

	public boolean isRegistrationFlow() {
		return isRegistrationFlow;
	}

	public void setRegistrationFlow(boolean isRegistrationFlow) {
		this.isRegistrationFlow = isRegistrationFlow;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
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

	@Override
	public String getPassword() {
		return password;
	}

	@Override
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

	@Override
	public String getLoginId() {
		return loginId;
	}

	@Override
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

	@Override
	public String getMotp() {
		return motp;
	}

	@Override
	public void setMotp(String motp) {
		this.motp = motp;
	}

	@Override
	public String getEotp() {
		return eotp;
	}

	@Override
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

	public CustomerFlags getFlags() {
		return flags;
	}

	public void setFlags(CustomerFlags flags) {
		this.flags = flags;
	}

}
