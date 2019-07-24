package com.amx.jax.model.response.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerFlags {

	Boolean fingerprintlinked;

	/* eKYC */
	String idProofStatus;
	Boolean idProofRequired;
	Boolean idProofVerificationPending;
	/* eKYC */

	/* new login */
	Boolean whatsAppVerified;
	Boolean emailVerified;
	Boolean mobileVerified;

	Boolean securityQuestionDone;
	/* new login */

	Boolean annualIncomeExpired;
	Boolean isDeactivated;
	Boolean isOnlineCustomer;
	Boolean isForceUpdateInsuranceRequired = false;
	Boolean isInsuranceActive = false;
	
	public Boolean annualTransactionLimitExpired;
	

	public Boolean getAnnualTransactionLimitExpired() {
		return annualTransactionLimitExpired;
	}

	public void setAnnualTransactionLimitExpired(Boolean annualTransactionLimitExpired) {
		this.annualTransactionLimitExpired = annualTransactionLimitExpired;
	}

	public Boolean isEmailMissing;

	public Boolean getIsEmailMissing() {
		return isEmailMissing;
	}

	public void setIsEmailMissing(Boolean isEmailMissing) {
		this.isEmailMissing = isEmailMissing;
	}

	public Boolean getIsOnlineCustomer() {
		return isOnlineCustomer;
	}

	public void setIsOnlineCustomer(Boolean isOnlineCustomer) {
		this.isOnlineCustomer = isOnlineCustomer;
	}

	public Boolean getFingerprintlinked() {
		return fingerprintlinked;
	}

	public void setFingerprintlinked(Boolean fingerprintlinked) {
		this.fingerprintlinked = fingerprintlinked;
	}

	public String getIdProofStatus() {
		return idProofStatus;
	}

	public void setIdProofStatus(String idProofStatus) {
		this.idProofStatus = idProofStatus;
	}

	public Boolean getWhatsAppVerified() {
		return whatsAppVerified;
	}

	public void setWhatsAppVerified(Boolean status) {
		this.whatsAppVerified = status;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean status) {
		this.emailVerified = status;
	}

	public Boolean getMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(Boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public Boolean getIdProofRequired() {
		return idProofRequired;
	}

	public void setIdProofRequired(Boolean idProofRequired) {
		this.idProofRequired = idProofRequired;
	}

	public Boolean getIdProofVerificationPending() {
		return idProofVerificationPending;
	}

	public void setIdProofVerificationPending(Boolean idProofVerificationPending) {
		this.idProofVerificationPending = idProofVerificationPending;
	}

	public Boolean getSecurityQuestionDone() {
		return securityQuestionDone;
	}

	public void setSecurityQuestionDone(Boolean securityQuestionDone) {
		this.securityQuestionDone = securityQuestionDone;
	}

	public Boolean getAnnualIncomeExpired() {
		return annualIncomeExpired;
	}

	public void setAnnualIncomeExpired(Boolean annualIncomeExpired) {
		this.annualIncomeExpired = annualIncomeExpired;
	}

	public Boolean getIsForceUpdateInsuranceRequired() {
		return isForceUpdateInsuranceRequired;
	}

	public Boolean getIsDeactivated() {
		return isDeactivated;
	}

	public void setIsForceUpdateInsuranceRequired(Boolean isForceUpdateInsuranceRequired) {
		this.isForceUpdateInsuranceRequired = isForceUpdateInsuranceRequired;
	}

	public void setIsDeactivated(Boolean isDeactivated) {
		this.isDeactivated = isDeactivated;
	}

	@Deprecated
	public Boolean getDisplayInsuranceDetail() {
		return isInsuranceActive;
	}

	@Deprecated
	public void setDisplayInsuranceDetail(Boolean displayInsuranceDetail) {
		this.isInsuranceActive = displayInsuranceDetail;
	}

	public Boolean getIsInsuranceActive() {
		return isInsuranceActive;
	}

	public void setIsInsuranceActive(Boolean isInsuranceActive) {
		this.isInsuranceActive = isInsuranceActive;
	}

}
