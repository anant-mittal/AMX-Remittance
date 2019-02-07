package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

import com.amx.jax.model.response.fx.UserStockDto;

public class RemittanceCollectionDto {

	BigDecimal paymentModeId;
	BigDecimal paymentAmount;
	String chequeBankCode;
	private String posBankCode;
	private String colchequeDate;
	private String colchequeRefNo;
	private String approvalNo;
	private String colCardHolderName;
	UserStockDto currencyDenomination = new UserStockDto();
	public BigDecimal getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(BigDecimal paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	public String getPosBankCode() {
		return posBankCode;
	}
	public void setPosBankCode(String posBankCode) {
		this.posBankCode = posBankCode;
	}
	
	public String getColchequeRefNo() {
		return colchequeRefNo;
	}
	public void setColchequeRefNo(String colchequeRefNo) {
		this.colchequeRefNo = colchequeRefNo;
	}
	
	public String getColCardHolderName() {
		return colCardHolderName;
	}
	public void setColCardHolderName(String colCardHolderName) {
		this.colCardHolderName = colCardHolderName;
	}
	public UserStockDto getCurrencyDenomination() {
		return currencyDenomination;
	}
	public void setCurrencyDenomination(UserStockDto currencyDenomination) {
		this.currencyDenomination = currencyDenomination;
	}
	public String getApprovalNo() {
		return approvalNo;
	}
	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}
	public String getChequeBankCode() {
		return chequeBankCode;
	}
	public void setChequeBankCode(String chequeBankCode) {
		this.chequeBankCode = chequeBankCode;
	}
	public String getColchequeDate() {
		return colchequeDate;
	}
	public void setColchequeDate(String colchequeDate) {
		this.colchequeDate = colchequeDate;
	}

	
	
}
