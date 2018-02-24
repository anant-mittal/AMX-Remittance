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
	BigDecimal netAmount;
	TransactionHistroyDTO transactionHistroyDTO;

	public JaxTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(JaxTransactionStatus status) {
		this.status = status;
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

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

}
