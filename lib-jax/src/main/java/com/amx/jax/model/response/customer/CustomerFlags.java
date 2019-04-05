package com.amx.jax.model.response.customer;

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

	Boolean securityQuestionRequired;
	Boolean securityAnswerRequired;
	/* new login */

	public Boolean annualIncomeExpired;

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

	public void setWhatsAppVerified(Boolean whatsAppVerified) {
		this.whatsAppVerified = whatsAppVerified;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
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

	public Boolean getSecurityQuestionRequired() {
		return securityQuestionRequired;
	}

	public void setSecurityQuestionRequired(Boolean securityQuestionRequired) {
		this.securityQuestionRequired = securityQuestionRequired;
	}

	public Boolean getSecurityAnswerRequired() {
		return securityAnswerRequired;
	}

	public void setSecurityAnswerRequired(Boolean securityAnswerRequired) {
		this.securityAnswerRequired = securityAnswerRequired;
	}

	public Boolean getAnnualIncomeExpired() {
		return annualIncomeExpired;
	}

	public void setAnnualIncomeExpired(Boolean annualIncomeExpired) {
		this.annualIncomeExpired = annualIncomeExpired;
	}
}
