package com.amx.jax.model.response.customer;

public class CustomerFlags {

	public String idProofStatus;
	public Boolean fingerprintlinked;
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

	public Boolean getAnnualIncomeExpired() {
		return annualIncomeExpired;
	}

	public void setAnnualIncomeExpired(Boolean annualIncomeExpired) {
		this.annualIncomeExpired = annualIncomeExpired;
	}

}
