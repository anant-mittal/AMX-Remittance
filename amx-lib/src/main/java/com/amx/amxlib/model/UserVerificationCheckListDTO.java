package com.amx.amxlib.model;

import com.amx.jax.model.AbstractModel;

public class UserVerificationCheckListDTO extends AbstractModel {

	private String loginId;
	private String mobileVerified;
	private String emailVerified;
	private String sequrityQuestion;
	private String sequriryPhishingImage;
	private String termsAndCondtion;
	private String kycVerified;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(String mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public String getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getSequrityQuestion() {
		return sequrityQuestion;
	}

	public void setSequrityQuestion(String sequrityQuestion) {
		this.sequrityQuestion = sequrityQuestion;
	}

	public String getSequriryPhishingImage() {
		return sequriryPhishingImage;
	}

	public void setSequriryPhishingImage(String sequriryPhishingImage) {
		this.sequriryPhishingImage = sequriryPhishingImage;
	}

	public String getTermsAndCondtion() {
		return termsAndCondtion;
	}

	public void setTermsAndCondtion(String termsAndCondtion) {
		this.termsAndCondtion = termsAndCondtion;
	}

	public String getKycVerified() {
		return kycVerified;
	}

	public void setKycVerified(String kycVerified) {
		this.kycVerified = kycVerified;
	}

	@Override
	public String getModelType() {
		return "checklist";
	}

}
