package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class ConfigDto {

	public BigDecimal cashLimit;
	public BigDecimal passportLimit;
	public BigDecimal gccLimit;
	public BigDecimal todayTrnxAmount;
	public BigDecimal totalLimit; // cash,knet,cheque,banktransfer
	
	public BigDecimal getPassportLimit() {
		return passportLimit;
	}
	public void setPassportLimit(BigDecimal passportLimit) {
		this.passportLimit = passportLimit;
	}
	public BigDecimal getGccLimit() {
		return gccLimit;
	}
	public void setGccLimit(BigDecimal gccLimit) {
		this.gccLimit = gccLimit;
	}
	public BigDecimal getCashLimit() {
		return cashLimit;
	}
	public void setCashLimit(BigDecimal cashLimit) {
		this.cashLimit = cashLimit;
	}
	public BigDecimal getTodayTrnxAmount() {
		return todayTrnxAmount;
	}
	public void setTodayTrnxAmount(BigDecimal todayTrnxAmount) {
		this.todayTrnxAmount = todayTrnxAmount;
	}
	public BigDecimal getTotalLimit() {
		return totalLimit;
	}
	public void setTotalLimit(BigDecimal totalLimit) {
		this.totalLimit = totalLimit;
	}
	
}
