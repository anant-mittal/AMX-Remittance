package com.amx.jax.model.response.customer;

public class CustomerFlags {

	Boolean fingerprintlinked;

	String idProofStatus;
	Boolean idProofExpired;
	Boolean idProofUnavailable;
	Boolean idProofPendingVerification;

	Boolean whatsAppVerified;
	Boolean emailVerified;
	Boolean mobileVerified;

	Boolean securityQuestionSet;

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

	public Boolean getIdProofExpired() {
		return idProofExpired;
	}

	public void setIdProofExpired(Boolean idProofExpired) {
		this.idProofExpired = idProofExpired;
	}

	public Boolean getIdProofUnavailable() {
		return idProofUnavailable;
	}

	public void setIdProofUnavailable(Boolean idProofUnavailable) {
		this.idProofUnavailable = idProofUnavailable;
	}

	public Boolean getIdProofPendingVerification() {
		return idProofPendingVerification;
	}

	public void setIdProofPendingVerification(Boolean idProofPendingVerification) {
		this.idProofPendingVerification = idProofPendingVerification;
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

	public Boolean getSecurityQuestionSet() {
		return securityQuestionSet;
	}

	public void setSecurityQuestionSet(Boolean securityQuestionSet) {
		this.securityQuestionSet = securityQuestionSet;
	}

	public Boolean getAnnualIncomeExpired() {
		return annualIncomeExpired;
	}

	public void setAnnualIncomeExpired(Boolean annualIncomeExpired) {
		this.annualIncomeExpired = annualIncomeExpired;
	}
}
