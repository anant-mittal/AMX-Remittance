package com.amx.jax.dbmodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;

@Entity
@Table(name = "JAX_USER_CHECKLIST")
public class UserVerificationCheckListModel extends SerializableSerializer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8360731811464042446L;
	@Id
	@Column(name = "LOGIN_ID")
	private String loginId;
	@Column(name = "MOBILE_VERIFIED")
	private String mobileVerified;
	@Column(name = "EMAIL_VERIFIED")
	private String emailVerified;
	@Column(name = "SEQURITY_QA")
	private String sequrityQuestion;
	@Column(name = "SEQURITY_PI")
	private String sequriryPhishingImage;
	@Column(name = "TERMS_CONDITION")
	private String termsAndCondtion;
	@Column(name = "KYC")
	private String kycVerified;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "UPDAED_DATE")
	private Date modifiedDate;
	@Column(name = "ISACTIVE")
	private String isActive;

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@PrePersist
	public void prepersist() {
		this.modifiedDate = new Date();
	}

}
