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
}
