package com.amx.jax.device;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.IDeviceStateData;

public class SignaturePadRemittanceInfo implements IDeviceStateData {

	@NotNull
	String name;
	@NotNull
	String bankName;
	@NotNull
	String branchName;
	@NotNull
	String accountNo;
	@NotNull
	String currencyAndAmount;
	@NotNull
	String exchangeRate;
	@NotNull
	String amount;
	@NotNull
	String comission;
	@NotNull
	String otherCharges;
	@NotNull
	String totalAmount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCurrencyAndAmount() {
		return currencyAndAmount;
	}

	public void setCurrencyAndAmount(String currencyAndAmount) {
		this.currencyAndAmount = currencyAndAmount;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getComission() {
		return comission;
	}

	public void setComission(String comission) {
		this.comission = comission;
	}

	public String getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
