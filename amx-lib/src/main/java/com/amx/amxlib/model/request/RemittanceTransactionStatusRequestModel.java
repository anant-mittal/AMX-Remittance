package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class RemittanceTransactionStatusRequestModel {

	BigDecimal applicationDocumentNumber;
	BigDecimal documentFinancialYear;
	
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
	
}
