package com.amx.jax.model.response.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.api.ResponseCodeDetailDTO;
import com.amx.jax.constants.JaxTransactionStatus;

public class PaymentLinkRespStatus implements Serializable {

	private static final long serialVersionUID = 2315791709068216697L;

	JaxTransactionStatus status;
	BigDecimal netAmount;
	String transactionReference;
	String errorMessage;
	String errorCategory;
	ResponseCodeDetailDTO responseCodeDetail;

	public JaxTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(JaxTransactionStatus status) {
		this.status = status;
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

	public ResponseCodeDetailDTO getResponseCodeDetail() {
		return responseCodeDetail;
	}

	public void setResponseCodeDetail(ResponseCodeDetailDTO responseCodeDetail) {
		this.responseCodeDetail = responseCodeDetail;
	}

}
