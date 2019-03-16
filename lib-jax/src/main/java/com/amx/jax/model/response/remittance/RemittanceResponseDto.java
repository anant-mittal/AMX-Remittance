package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class RemittanceResponseDto {

	private String receiptNo;
	private BigDecimal collectionDocumentNo;
	private BigDecimal collectionDocumentFYear;
	private BigDecimal collectionDocumentCode;
	private boolean declarationReport=false;
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public BigDecimal getCollectionDocumentNo() {
		return collectionDocumentNo;
	}
	public void setCollectionDocumentNo(BigDecimal collectionDocumentNo) {
		this.collectionDocumentNo = collectionDocumentNo;
	}
	public BigDecimal getCollectionDocumentFYear() {
		return collectionDocumentFYear;
	}
	public void setCollectionDocumentFYear(BigDecimal collectionDocumentFYear) {
		this.collectionDocumentFYear = collectionDocumentFYear;
	}
	public BigDecimal getCollectionDocumentCode() {
		return collectionDocumentCode;
	}
	public void setCollectionDocumentCode(BigDecimal collectionDocumentCode) {
		this.collectionDocumentCode = collectionDocumentCode;
	}
	public boolean isDeclarationReport() {
		return declarationReport;
	}
	public void setDeclarationReport(boolean declarationReport) {
		this.declarationReport = declarationReport;
	}
	
}
