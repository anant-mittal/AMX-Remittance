package com.amx.amxlib.model.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RemittanceTransactionStatusRequestModel {

	BigDecimal applicationDocumentNumber;
	BigDecimal documentFinancialYear;
	@JsonIgnore
	Boolean promotion;
	
	public BigDecimal getApplicationDocumentNumber() {
		return applicationDocumentNumber;
	}
	public void setApplicationDocumentNumber(BigDecimal applicationDocumentNumber) {
		this.applicationDocumentNumber = applicationDocumentNumber;
	}
	public BigDecimal getDocumentFinancialYear() {
		return documentFinancialYear;
	}
	public void setDocumentFinancialYear(BigDecimal documentFinancialYear) {
		this.documentFinancialYear = documentFinancialYear;
	}
	@Override
	public String toString() {
		return "RemittanceTransactionStatusRequestModel [applicationDocumentNumber=" + applicationDocumentNumber
				+ ", documentFinancialYear=" + documentFinancialYear + "]";
	}
	public Boolean getPromotion() {
		return promotion;
	}
	public void setPromotion(Boolean promotion) {
		this.promotion = promotion;
	}
	
}
