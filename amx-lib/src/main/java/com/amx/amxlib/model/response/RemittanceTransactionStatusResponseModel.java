package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.constant.JaxTransactionStatus;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.AbstractModel;

public class RemittanceTransactionStatusResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JaxTransactionStatus status;
	BigDecimal collectionDocumentNumber;
	BigDecimal amountDeducted;
	TransactionHistroyDTO transactionHistroyDTO;

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

	public TransactionHistroyDTO getTransactionHistroyDTO() {
		return transactionHistroyDTO;
	}

	public void setTransactionHistroyDTO(TransactionHistroyDTO transactionHistroyDTO) {
		this.transactionHistroyDTO = transactionHistroyDTO;
	}

}
