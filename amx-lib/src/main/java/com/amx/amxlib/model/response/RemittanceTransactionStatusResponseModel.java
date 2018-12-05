package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.PromotionDto;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.model.AbstractModel;

public class RemittanceTransactionStatusResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JaxTransactionStatus status;
	BigDecimal netAmount;
	TransactionHistroyDTO transactionHistroyDTO;
	String transactionReference;
	String errorMessage;
	String errorCategory;
	PromotionDto promotionDto; 

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

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCategory() {
		return errorCategory;
	}

	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}
	
	public PromotionDto getPromotionDto() {
		return promotionDto;
	}

	public void setPromotionDto(PromotionDto promotionDto) {
		this.promotionDto = promotionDto;
	}

}
