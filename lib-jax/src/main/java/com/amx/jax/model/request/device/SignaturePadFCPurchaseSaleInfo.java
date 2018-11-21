package com.amx.jax.model.request.device;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.IDeviceStateData;

public class SignaturePadFCPurchaseSaleInfo implements IDeviceStateData {

	private static final long serialVersionUID = 3747803007432987595L;

	@NotNull(message = "Source of Funds may not be null")
	private String sourceOfFunds;

	@NotNull(message = "Purpose of Txn may not be null")
	private String purposeOfTxn;

	@NotNull(message = "CCY and Amt may not be null")
	private String ccyAndAmt;

	@NotNull(message = "Exchange Rate may not be null")
	private String exchangeRate;

	@NotNull(message = "Kd Amount may not be null")
	private String kdAmount;

	@NotNull(message = "Total Amount may not be null")
	private String totalAmount;

	public String getSourceOfFunds() {
		return sourceOfFunds;
	}

	public void setSourceOfFunds(String sourceOfFunds) {
		this.sourceOfFunds = sourceOfFunds;
	}

	public String getPurposeOfTxn() {
		return purposeOfTxn;
	}

	public void setPurposeOfTxn(String purposeOfTxn) {
		this.purposeOfTxn = purposeOfTxn;
	}

	public String getCcyAndAmt() {
		return ccyAndAmt;
	}

	public void setCcyAndAmt(String ccyAndAmt) {
		this.ccyAndAmt = ccyAndAmt;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getKdAmount() {
		return kdAmount;
	}

	public void setKdAmount(String kdAmount) {
		this.kdAmount = kdAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
