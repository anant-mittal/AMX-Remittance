package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;




public class FxOrderTransactionStatusResponseDto extends AbstractModel{
	
	private static final long serialVersionUID = 1L;

	//JaxTransactionStatus status;
	BigDecimal netAmount;
	FxOrderTransactionHistroyDto fxOrderTrnxHistroyDTO;
	String transactionReference;
	String errorMessage;
	String errorCategory;
	public BigDecimal getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}
	public FxOrderTransactionHistroyDto getFxOrderTrnxHistroyDTO() {
		return fxOrderTrnxHistroyDTO;
	}
	public void setFxOrderTrnxHistroyDTO(
			FxOrderTransactionHistroyDto fxOrderTrnxHistroyDTO) {
		this.fxOrderTrnxHistroyDTO = fxOrderTrnxHistroyDTO;
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
	

}
