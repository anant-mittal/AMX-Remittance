package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.model.AbstractModel;




public class FxOrderTransactionStatusResponseDto extends AbstractModel{
	
	private static final long serialVersionUID = 1L;

	JaxTransactionStatus status;
	BigDecimal netAmount;
	List<FxOrderTransactionHistroyDto>  fxOrderTrnxHistroyDTO;
	
	String transactionReference;
	String errorMessage;
	String errorCategory;
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
	public JaxTransactionStatus getStatus() {
		return status;
	}
	public void setStatus(JaxTransactionStatus status) {
		this.status = status;
	}
	public List<FxOrderTransactionHistroyDto> getFxOrderTrnxHistroyDTO() {
		return fxOrderTrnxHistroyDTO;
	}
	public void setFxOrderTrnxHistroyDTO(
			List<FxOrderTransactionHistroyDto> fxOrderTrnxHistroyDTO) {
		this.fxOrderTrnxHistroyDTO = fxOrderTrnxHistroyDTO;
	}
	

}
