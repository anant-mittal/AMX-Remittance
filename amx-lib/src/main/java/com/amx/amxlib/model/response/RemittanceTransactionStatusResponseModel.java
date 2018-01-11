package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.constant.JaxTransactionStatus;
import com.amx.amxlib.model.AbstractModel;

public class RemittanceTransactionStatusResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JaxTransactionStatus status;
	BigDecimal collectionDocumentNumber;
	BigDecimal amountDeducted;
	private BigDecimal documentFinanceYear;
	private BigDecimal collectionDocumentCode;
	private BigDecimal customerReference;

	public JaxTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(JaxTransactionStatus status) {
		this.status = status;
	}

	public BigDecimal getCollectionDocumentNumber() {
		return collectionDocumentNumber;
	}

	public void setCollectionDocumentNumber(BigDecimal collectionDocumentNumber) {
		this.collectionDocumentNumber = collectionDocumentNumber;
	}

	public BigDecimal getAmountDeducted() {
		return amountDeducted;
	}

	public void setAmountDeducted(BigDecimal amountDeducted) {
		this.amountDeducted = amountDeducted;
	}

	@Override
	public String getModelType() {
		return "remittance-transaction-status-model";
	}

	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}

	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}

	public BigDecimal getCollectionDocumentCode() {
		return collectionDocumentCode;
	}

	public void setCollectionDocumentCode(BigDecimal collectionDocumentCode) {
		this.collectionDocumentCode = collectionDocumentCode;
	}

	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

}
